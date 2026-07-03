#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

struct Cerinta{
        char s[100];
        uint16_t len,l,i;
};

int main(){
	int s;
	struct sockaddr_in server,client;
	int c,l;
	struct Cerinta data;

	s=socket(AF_INET,SOCK_DGRAM,0);
	if(s<0){
		printf("Eroare la crearea socketului server\n");
		return 1;
	}

	memset(&server,0,sizeof(server));
	server.sin_port=htons(1234);
	server.sin_family=AF_INET;
	server.sin_addr.s_addr=INADDR_ANY;

	if(bind(s,(struct sockaddr *) &server,sizeof(server))<0){
		printf("Eroare la bind\n");
		return 1;
	}
	while(1){
	l=sizeof(client);
	memset(&client,0,sizeof(client));
	
	
	recvfrom(s,&data,sizeof(data),MSG_WAITALL,(struct sockaddr *) &client,&l);

	data.i=ntohs(data.i);
	data.len=ntohs(data.len);
	data.l=ntohs(data.l);

	if(data.i>=data.len)
		data.l=0;
	else if(data.i+data.l>data.len)
		data.l=data.len-data.i;

	sendto(s,data.s+data.i,data.l,0,(struct sockaddr *) &client,sizeof(client));
	}
	close(s);
}
