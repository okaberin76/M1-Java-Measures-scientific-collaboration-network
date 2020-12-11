set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'distributionDegresLineaire_Default.png'
set key top right
set yrange [1e-6:1]

plot  "fichierDistributionDegree_Default.dat" using 1:2 title 'Distribution des degrés linéaire' with lines


