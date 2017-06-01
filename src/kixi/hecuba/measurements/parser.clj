(ns kixi.hecuba.measurements.parser
  (:require [clojure.data.xml :as xml]
            [com.rpl.specter :as specter]))

(defn has-tag?
  [tag element]
  (= (:tag element) tag))

(defn gas-or-electricity?
  [element]
  (or (has-tag? :Electricity element) (has-tag? :Gas element)))

(defn correlation-id
  [parsed]
  (->> parsed
       (specter/select-one [:content
                            specter/ALL
                            (partial has-tag? :CorrelationID)
                            :content])
       first))

(defn measurement-type
  [measurement]
  (get
   {:Electricity "electricityConsumption"
    :Gas "gasConsumption"}
   (specter/select-one [:content
                        specter/ALL
                        (partial gas-or-electricity?)
                        :tag] measurement)))

(defn measurement-value
  [measurement]
  (first (specter/select-one [:content
                              specter/ALL
                              (partial gas-or-electricity?)
                              :content
                              specter/ALL
                              (partial has-tag? :PrimaryValue)
                              :content] measurement)))

(defn measurement-timestamp
  [measurement]
  (first (specter/select-one [:content
                              specter/ALL
                              (partial has-tag? :Timestamp)
                              :content] measurement)))

(defn measurement-information
  [measurement]
  (hash-map :type (measurement-type measurement)
            :value (measurement-value measurement)
            :timestamp (measurement-timestamp measurement))
  )
(defn measurements
  [parsed]
  (->> parsed
       (specter/select [:content
                        specter/ALL
                        (partial has-tag? :Body)
                        :content
                        specter/ALL
                        :content
                        specter/ALL])
       (mapv measurement-information)))



(defn parse
  [data-in]
  (xml/parse (java.io.StringReader. data-in)))

(defn process
  "parse measurement xml and extract relevant data"
  [data-in]
  (let [parsed (parse data-in)]
    (hash-map :measurements (measurements parsed)
              :correlation-id (correlation-id parsed))))
