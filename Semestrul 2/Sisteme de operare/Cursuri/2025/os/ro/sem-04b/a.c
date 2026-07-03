int stubborn_read(int fd, char* buf, int length, int trials) {
    int total = 0, n = 0, k;
    while(total < length && n < trials && (k = read(fd, buf+total, length-total)) > 0) {
        total += k;
        n++;
    }

    return k<0 ? k : total;
}

int stubborn_write(int fd, char* buf, int length, int trials) {
    int total = 0, n = 0, k;
    while(total < length && n < trials && (k = write(fd, buf+total, length-total)) > 0) {
        total += k;
        n++;
    }

    return k<0 ? k : total;
}
