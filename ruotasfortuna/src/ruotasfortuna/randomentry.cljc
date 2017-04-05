(ns ruotasfortuna.randomentry)

; Queste funzioni si aspettano
; una mappa di chiavi e pesi (wmap)
; di cui fare l'estrazione casuale.
;
;



(defn sum-weights
  [wmap]
  (reduce + (vals wmap)))


(defn findPos
  "
    Dato un numero ed una sequenza di numeri
    che sono dei delta, trova il punto della sequenza
    dove cade il numero stesso.

    Se il numero è oltre la somma della
    sequenza, ritorna la posizione dell'ultimo
    elemento.

    Per farlo, confronta il valore di taglio con
    l'elemento corrente; se è piu' basso, ho trovato
    il mio eleento; se no, sottraggo
    il valore dell'elemento al valore di taglio e
    provo il successivo.


  "
  [point lVals]
  (loop [pos  0
         thr-left point
         vals lVals]

    (let [v (first vals)
          r (rest vals)]
           (cond

             (< thr-left v)
                pos

             (empty? r)
               pos

             :else
                (recur
                    (inc pos)
                    (- thr-left v)
                    r)))))




(defn extract
  "
  Effettua un'estrazione casuale da:
   {:a 1 :b 2}
  :a viene estratta con probabilita' 1/3
  e :b con probabilita' 2/3.

  Per poterla testare, ho una vesione ad arity-2
  che riceve il 'numero casuale'.
  "

  ([wmap]
   (extract wmap (rand)))

  ([wmap random-number]
   (let [limit (* random-number (sum-weights wmap))
         ks  (keys wmap)
         vs  (vals wmap)
         pos (findPos limit vs)
         ]
     (nth ks pos)
     )))



(defn upgrade-weights
  "
    Aggiorna il peso di un fattore della
    mappa di pesi.
  "
  [wmap entry]
  (let [old-val  (get wmap entry 0)
        new-val  (/ old-val 2)]
    (assoc wmap entry new-val)))





(defn estrae-vittime-con-upgrade
  "
  Genera un vettore di vittime per i casi passati.
  Ad ogni estrazione, riduce la probabilita'
  per una vittima data.
  "
  [wmap casi]
  (loop [wm wmap
         rc casi
         acc []]
    (cond
      (empty? rc)
      acc
      :else
      (let [vittima (extract wm)
            new-wmap (upgrade-weights wm vittima)
            ]
        (recur new-wmap (rest rc) (conj acc vittima))

        )
      )
    )
  )




