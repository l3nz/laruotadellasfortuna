# ruotasfortuna

## REPL


(ns ruotasfortuna.core)
ruotasfortuna.core=> @app-state

## RUN


rlwrap ~/lenz_bin/lein figwheel


## Compile prod

~/lenz_bin/lein clean
~/lenz_bin/lein cljsbuild once min


## Testing && linting


    lein test && lein eastwood && lein bikeshed && lein docstring-checker

Not sure bikeshed works.

## Docs

    lein codox


