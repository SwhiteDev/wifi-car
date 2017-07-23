CC = gcc
CFLAGS = -g -Wall
GTK_FLAGS = `pkg-config --cflags gtk+-3.0 gmodule-2.0`
LIBS = `pkg-config --libs gtk+-3.0 gmodule-2.0`

SRC = socket.c
SRC += gtk.c

OBJS = $(patsubst %.c, %.o, $(SRC))

ALL : socket gtk


gtk : gtk.c 
	$(CC) $(CFLAGS) $(GTK_FLAGS) -o $@ $^ $(LIBS)

socket : socket.c
	$(CC) $(CFLAGS) -o $@ $^

%.o : %.c
	$(CC) $(CFLAGS) $(GTK_FLAGS) -c $< $(LIBS)

.PHONY : clean
clean :
	rm -rf wifi-car *.o
