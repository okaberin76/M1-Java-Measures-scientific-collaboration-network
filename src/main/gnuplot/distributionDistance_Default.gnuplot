set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'distributionDistance_Default.PNG'
set title 'Distribution des distances - Default'

set xlabel 'd'
set ylabel 'p(d)'
set xrange[0:20]

plot  "fichierDistributionDistance_Default.dat" using 1:2 title 'Distribution distances' with lines
