(ns healthinsurance.core
  (:require [clojure.string :as str]))

(def data
  {:wife [#_ [franchise monthly-installment]
          [300 357.70]
          [500 346.10]
          [1000 316.30]
          [1500 287.70]
          [2000 258.60]
          [2500 229.40]]

   :husband [[300 339.90]
             [500 328.30]
             [1000 301.10]
             [1500 273.40]
             [2000 245.70]
             [2500 218]]

   :kid [[0 81]
         [100 80]
         [200 79]
         [300 78]]})

(def healthcare-costs (range 100 2600 100))
(def liabilities [300 500 1000 1500 2000 2500])

;; have a function to write the datastructure to a CSV
#_(def headers ["name and liability" "monthly payment" ...a range of costs...])

(defn- calculate-yearly-payment [monthly-cost liability consultancy-cost]
  (let [yearly-base-cost (* 12 monthly-cost)]
    (+ (if (< consultancy-cost liability)
         consultancy-cost
         (+ liability (* 0.1 (- consultancy-cost liability))))
      yearly-base-cost)))

(comment
  #_ (calculate-yearly-payment 357.7 300 200)
  #_ (calculate-yearly-payment 357.7 300 400)
  )

(dorun
  (let [individuals (keys data)]
    (dorun
      (for [individual individuals]
        (dorun
          (for [liability liabilities]
            (let [monthly-cost (->> (individual data)
                                 (filter #(= liability (nth % 0)))
                                 first
                                 (#(nth % 1)))]
              (println (str/join ","
                         (into []
                           (concat
                             [(str (name individual) "_" liability)
                              monthly-cost]
                             (mapv (fn [consultancy-cost]
                                     (calculate-yearly-payment monthly-cost liability consultancy-cost))
                               healthcare-costs))))))))))))
