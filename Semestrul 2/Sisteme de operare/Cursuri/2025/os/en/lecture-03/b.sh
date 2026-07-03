#!/bin/bash

F=$1

N=0
while [ $N -lt 3 ]; do
    K=`cat $F`
    K=`expr $K + 1`
    echo $K > $F
    N=`expr $N + 1`
done
