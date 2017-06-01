(ns kixi.hecuba.measurements.parser-test
  (:require [kixi.hecuba.measurements.parser :refer :all]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(def measurement-string (slurp (io/resource "test/measurement.xml")))

(deftest parser-test
  (testing "parsing expected measurement xml structure"
    (is (= (process measurement-string) nil))))
