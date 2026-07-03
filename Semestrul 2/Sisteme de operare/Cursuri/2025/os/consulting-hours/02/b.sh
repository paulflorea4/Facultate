#!/bin/bash -x

D=$1

find $D -type f > files.txt
N=0
while read F; do
    sha1sum $F
done < files.txt > checksums.txt

while read C1 F1; do
    while read C2 F2; do
        if [ "$F1" != "$F2" ] && [ "$C1" == "$C2" ]; then
            echo "$F1" "$F2"
        fi
    done < checksums.txt
done < checksums.txt
