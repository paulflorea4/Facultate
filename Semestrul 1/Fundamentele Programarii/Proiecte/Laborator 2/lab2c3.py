def anBisect(an):
    if an%400==0:
        return True
    if an%4==0 and an%100!=0:
        return True
    return False
        
an=int(input("An:"))
ziOrd=int(input("Ziua de ordine:"))
luna=1
if anBisect(an)==True:
    lunileAnului=[31,29,31,30,31,30,31,31,30,31,30,31]
else:
    lunileAnului=[31,28,31,30,31,30,31,31,30,31,30,31]
print(lunileAnului[1])
for l in lunileAnului:
    if ziOrd-l>0:
        luna=luna+1
        ziOrd=ziOrd-l
    else:
        zi=ziOrd
        break
print(f"{zi}/{luna}/{an}");
