#!/bin/bash

find a -type f -perm -o=w | while read F; do
    ls -l $F
    chmod o-w $F
    ls -l $F
    echo "========================================"
done
