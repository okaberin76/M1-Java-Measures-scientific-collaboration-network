set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'distributionDegresLineaire.png'
set key top right
set yrange [1e-6:1]

plot  "fichierDistributionDegree.dat" using 1:2 title 'Distribution des degrés linéaire' with lines


