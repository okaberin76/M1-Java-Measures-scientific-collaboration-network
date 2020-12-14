set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'distributionDistance_Barabasi.PNG'
set title 'Distribution des distances - Barabasi'

set xlabel 'd'
set ylabel 'p(d)'
set xrange[0:10]

plot 'fichierDistributionDistance_Barabasi.dat' using 1:2 title 'Distribution distances' with lines
