set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'distributionDistance_Random.PNG'
set title 'Distribution des distances - Random'
set key top right

set logscale xy
set yrange [1e-6:1]

# Poisson
lambda = 6.62208890914917
poisson(k) = lambda ** k * exp(-lambda) / gamma(k + 1)

# Fit une fonction linéaire en log-log
f(x) = lc - gamma * x
fit f(x) 'fichierDistributionDistance_Random.dat' using (log($1)):(log($2)) via lc, gamma

c = exp(lc)
power(k) = c * k ** (-gamma)

plot 'fichierDistributionDistance_Random.dat' title 'Distribution des distances', poisson(x) title 'Loi de Poisson', power(x) title 'Loi de puissance'
