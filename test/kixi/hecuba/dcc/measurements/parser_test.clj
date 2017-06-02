(ns kixi.hecuba.dcc.measurements.parser-test
  (:require [kixi.hecuba.dcc.measurements.parser :refer :all]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(def measurement-string (slurp (io/resource "test/measurement.xml")))
(def nonsense-string "testtesttest")
(def incomplete-xml (slurp (io/resource "test/measurement-broken.xml")))

(deftest parser-test
  (testing "parsing expected measurement xml structure"
    (let [measurement-data (:measurements (process measurement-string))]
      (is (== (count measurement-data) 49))
      (is (every? #(number? (bigdec (:value %))) measurement-data))
      (is (every? #(#{"electricityConsumption" "gasConsumption"} (:type %)) measurement-data))))
  (testing "parsing broken xml gives appropriate error"
    (is nil (process nonsense-string))
    (is nil (process incomplete-xml)))
  (testing "parsing xml with invalid value gives error"))
