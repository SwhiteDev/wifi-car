#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#define CLEAR(x) memset(&x, 0, sizeof(x))

static const int MAXPENDING = 5; /* Maximim outstanding connection requests */

void handle_tcp_client(int clnt_sock);

int main(int argc, char **argv)
{
	int    sockfd;
	int    clnt_sock;
	char   clnt_name[INET_ADDRSTRLEN]; /* String to contain client address */
	in_port_t serv_port;
	struct sockaddr_in serv_addr;

	if (argc != 2) {
		fprintf(stderr, "parameter error");
		exit(0);
	}

	serv_port = atoi(argv[1]); /* First arg: local port */

	/* Create socket for incoming connections */
	if ((sockfd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0) {
		perror("socket");
		exit(0);
	}
	printf("create socket ok\n");

	/* Construct local address strucure */
	CLEAR(serv_addr);
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY); /* Any incoming inerface */
	serv_addr.sin_port = htons(serv_port);
	

	/* Bind to the local address */
	if (bind(sockfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) < 0) {
		perror("bind");
		exit(0);
	}
	printf("Bind socket address ok\n");

	/* Mark the socket so it will listen for incoming connections */
	if (listen(sockfd, MAXPENDING) < 0) {
		perror("listen");
		exit(0);
	}
	printf("Listening\n");

	for (;;) {
		struct sockaddr_in clnt_addr;
		socklen_t clnt_addr_len = sizeof(clnt_addr); /* set length of client address structure (in-out parameter) */

		/* Wait for a client to connect */
		if ((clnt_sock = accept(sockfd, (struct sockaddr*)&clnt_addr, &clnt_addr_len)) < 0) {
			perror("accept");
			exit(0);
		}

		/* connect ok */
		
		if (inet_ntop(AF_INET, &clnt_addr.sin_addr.s_addr, clnt_name, sizeof(clnt_name)) != NULL) {
			printf("Handling client %s/%d\n", clnt_name, ntohs(clnt_addr.sin_port));
		}
		else {
			puts("Unable to get client address");
		}

		handle_tcp_client(clnt_sock);
	}
}


void handle_tcp_client(int clnt_sock)
{
	char    buffer[BUFSIZ]; /* Buffer for echo string */
    ssize_t nbytes_recv;
	ssize_t nbytes_send;

	if ((nbytes_recv = recv(clnt_sock, buffer, BUFSIZ, 0)) < 0) {
		perror("recv");
		exit(0);
	}

	printf("Receive message: %s\n", buffer);
	/* send received string and receive again until end of stream */
	while (nbytes_recv > 0) {
		if ((nbytes_send = send(clnt_sock, buffer, nbytes_recv, 0)) < 0) {
			perror("send");
			exit(0);
		}
		else if (nbytes_send != nbytes_recv) {
			fprintf(stderr, "Rent unexpected number of bytes");
		}

		/* See there is more data to receive */
		if ((nbytes_recv = recv(clnt_sock, buffer, BUFSIZ, 0)) < 0) {
			perror("recv");
			exit(0);
		}
	}

	close(clnt_sock);
}
