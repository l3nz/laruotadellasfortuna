# ruotasfortuna

## REPL


(ns ruotasfortuna.core)
ruotasfortuna.core=> @app-state

## RUN


rlwrap ~/lenz_bin/lein figwheel


## Compile prod

~/lenz_bin/lein clean
~/lenz_bin/lein cljsbuild once min

