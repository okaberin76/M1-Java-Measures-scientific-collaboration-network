set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'distributionDegresLog_Default.PNG'
set title 'Distribution log log des degrés - Default'
set key top right

set logscale xy
set yrange [1e-6:1]

plot 'fichierDistributionDegree_Default.dat' title 'Distribution des degrés'
