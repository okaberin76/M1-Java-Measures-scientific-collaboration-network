set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set yrange[0:0.2]
set output 'distributionDegresLineaire_Default.PNG'
set title 'Distribution linéaire des degrés'
set key top right

plot  "fichierDistributionDegree_Default.dat" using 1:2 title 'Distribution des degrés linéaire' with lines