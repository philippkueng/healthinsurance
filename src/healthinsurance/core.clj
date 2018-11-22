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
             [2500 218]]})

;; use this to create columns from 100 CHF to 2500 CHF in 100 CHF increments (we'll calculate the yearly cost against each of those entries)
(def healthcare-costs (range 100 2600 100))

;; the liabilities we cover in `data`, we could also extract that from `data` itself with it's own function.
(def liabilities [300 500 1000 1500 2000 2500])

(defn- calculate-yearly-payment
  "Given a the liability/franchise chosen and the given the costs one has incrued this year, calculate how much one has to pay overall for the whole year."
  [monthly-cost liability consultancy-cost]
  (let [yearly-base-cost (* 12 monthly-cost)]
    (+ (if (< consultancy-cost liability)
         consultancy-cost
         (+ liability (* 0.1 (- consultancy-cost liability))))
      yearly-base-cost)))

;; make sure to fill in the `data`, `healthcare-costs` and `liabilities` variables with your desired values
;; then execute the expression below and copy the output into a CSV file.
;; add some headers and open it excel and then apply some conditional formatting to see which liabilities might be favourable given your estimated yearly healtcare costs.
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
