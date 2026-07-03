BEGIN {
    s = 0
}

{s += $3}

END {
    print s
}
