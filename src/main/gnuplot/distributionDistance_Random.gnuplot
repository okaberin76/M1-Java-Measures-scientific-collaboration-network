set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'distributionDistance_Random.PNG'
set title 'Distribution des distances - Random'

set xlabel 'd'
set ylabel 'p(d)'
set xrange[0:12]

plot 'fichierDistributionDistance_Random.dat' using 1:2 title 'Distribution distances' with lines
