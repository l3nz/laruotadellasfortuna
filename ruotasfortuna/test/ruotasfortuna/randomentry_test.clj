(ns ruotasfortuna.randomentry-test
  (:use clojure.test)
  (:use ruotasfortuna.randomentry))

; ====================================================
; TESTS
; (in-ns 'livewator.core)
; (run-tests)



(def SRC {
  :A 10   ;   0-10
  :B 2    ;  10-12
  :C 7    ;  12-19
  :D 0    ;  19-19   (mai)
  :E 11   ;  19-30
          })

(deftest test-sum-weights
  (is (= 30 (sum-weights SRC))))


(deftest test-extract
  (is (= :A  (extract SRC 0)  ))
  (is (= :C  (extract SRC 0.5)  ))
  (is (= :E  (extract SRC 1)  ))
  )


(deftest test-upgrade-weights
  (is (= 5
         (:A (upgrade-weights SRC :A))  ))
  (is (= 0
         (:D (upgrade-weights SRC :D))  ))
  )




;              5 6 25 36 36 39
(def SOMEVALS [5 1 19 11 0  3])

(deftest test-findpos
  (is (= 0   (findPos 1 SOMEVALS)  ))
  (is (= 1   (findPos 5.5 SOMEVALS)  ))
  (is (= 2   (findPos 6.5 SOMEVALS)  ))
  (is (= 3   (findPos 25.5 SOMEVALS)  ))
  (is (= 5   (findPos 36.5 SOMEVALS)  ))
  (is (= 5   (findPos 38.5 SOMEVALS)  ))
  (is (= 5   (findPos 138.6 SOMEVALS)  ))
  )


(deftest test-estrai-vittime
  (let [rv (estrae-vittime-con-upgrade SRC  ["x" "y" "z"] )  ]
    (is (= 3 (count rv)))))
