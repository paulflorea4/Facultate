#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>

struct Cerinta{
	char s[100];
	uint16_t len,l,i;
};

int main(){
	int c;
	struct sockaddr_in server;
	struct Cerinta data;
	c=socket(AF_INET,SOCK_DGRAM,0);
	if(c<0){
		printf("Eroare la crearea socketului client\n");
		return 1;
	}

	memset(&server,0,sizeof(server));
	server.sin_port=htons(1234);
	server.sin_family=AF_INET;
	server.sin_addr.s_addr=inet_addr("127.0.0.1");

	printf("l = ");
	scanf("%hu",&data.l);

	printf("i = ");
	scanf("%hu",&data.i);

	printf("Introduceti un sir:\n");
	getchar();
	fgets(data.s,100,stdin);

	data.l=htons(data.l);
	data.i=htons(data.i);

	data.len=strlen(data.s);
	data.len=htons(data.len);

	sendto(c,&data,sizeof(data),0,(struct sockaddr *) &server,sizeof(server));

	char r[100];
	int server_len=sizeof(server);
	int n=recvfrom(c,&r,data.l,MSG_WAITALL,(struct sockaddr *) &server,&server_len);
	if(n>0){
		r[n]='\0';
		printf("%s\n",r);
	}

	close(c);
}
