(ns ruotasfortuna.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed. from src/ruotasfortuna/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(def VITTIME [
	{:nome "Leonardo"    :peso .45 :colore "blue"}
	{:nome "Leda Topia"  :peso .45 :colore "pink"}
	{:nome "Tutti&Due"   :peso .10 :colore "red"}
])

(def LOOPS-PER-SEC 30)
(def ALL-ITEMS    100)

(def EMPTY-STATE {
    :estrazione ""
    :victim    ""
    :image     ""
    :countdown ALL-ITEMS
    :sfortunometro {}
    })

(defonce app-state (atom EMPTY-STATE))

(defn weights
  "Trasforma i pesi di VITTIME in un array di pesi sommati ascendenti.

   es: [0.45 0.9 1]
  "
  [vittime]
	(let [pesi (vec (map :peso VITTIME))]
		(loop [p pesi s 0 somme []]
			(if (zero? (count p))
				somme

				(let [v (first p)
                      ss (+ s v) ]
                      (recur (vec (rest p)) ss (conj somme ss)) 
                 ) ))))


(defn whichitem
  "Dato un numero casuale e un array di pesi, quale elemento
   è stato scelto?
  "
  [rand weights]
	(loop [i 0 
	       w weights]
		(if (< rand (first w))
			i
			(recur (inc i) (rest w))
		)
	)
)



(defn extract
  "Effettua un'estrazione casuale"
  []
	(let [n   (rand)
          ws  (weights VITTIME)
          p   (whichitem n ws) ]
    (VITTIME p)
	))

(defn update-loop [state]
  (let [sfortunello (extract)]
    (-> state
        (update-in [:sfortunometro sfortunello] inc)
        (assoc-in [:victim] (:nome sfortunello) )
        (update-in [:countdown] dec)

        )
  ))


(defn do-countdown [items-left]
	(if (pos? items-left)
		(js/setTimeout
      #(swap! app-state update-loop)
      (/ 1000 LOOPS-PER-SEC)))
  ""
  )


(defn cmp-bottone-start [title]
  [:input {:type "button"
           :value title
           :on-click #(reset! app-state (assoc-in EMPTY-STATE [:estrazione] title))
           }])


(defn cmp-sfortunometro-entry [colore  numero]
  [:span {:style {:color colore} :key colore}
   (reduce str (take numero (repeat "x")))
   ])

(defn cmp-sfortunometro-titolo [nome colore percento]
  [:span {:style {:color colore :width "200px"} :key colore}
   (str nome ": "  (js/parseInt (* 100 percento)) "%")
   ])


(defn cmp-sfortunometro
  "
      [{:nome \"LEO\", :peso 0.45, :colore \"#FF0000\"} 44]
      [{:nome \"LEDA\", :peso 0.45, :colore \"#00FF00\"} 45]
      [{:nome \"TED\", :peso 0.1, :colore \"#0000FF\"} 11]
  "

  [ dati ]
  (let [curr-entries (reduce +
                             (map (fn [[_ v]] v) dati))
        missing-entries (- ALL-ITEMS curr-entries) ]


      [:div

       (cond (pos? curr-entries)


             [:div

              (map
                (fn [[k v]] (cmp-sfortunometro-titolo (:nome k) (:colore k) (/ v curr-entries)  ))
                dati)

              ]
             )

      [:div

       (map
         (fn [[k v]]  (cmp-sfortunometro-entry (:colore k) v)    )
         dati)

       (cmp-sfortunometro-entry "gray" missing-entries)
       ]

       ]

    )
  )

(defn main-app []
	(fn []
    (let [state @app-state]
		[:div
		 [:h1 (str (:estrazione state) ": " (:victim state))]
		 ;[:h2 (:countdown state)]
		 (cmp-bottone-start "Apparecchia")
     (cmp-bottone-start "Sparecchia")
     (cmp-bottone-start "Aspirapolvere")
     (cmp-bottone-start "Altro")


     (do-countdown (:countdown state))
     (cmp-sfortunometro (:sfortunometro state))
		 ])))

(reagent/render-component [main-app]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
