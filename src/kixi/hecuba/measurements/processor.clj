(ns kixi.hecuba.measurements.processor
  (:require [franzy.admin.zookeeper.defaults :as zk-defaults]
            [franzy.admin.zookeeper.client :as client]
            [franzy.admin.cluster :as cluster]
            [clojure.string :as cstr]
            [taoensso.timbre :as log]
            [environ.core :refer [env]])
  (:import [org.apache.kafka.streams.kstream KStreamBuilder ValueMapper]
           [org.apache.kafka.streams KafkaStreams StreamsConfig]
           [org.apache.kafka.common.serialization Serdes])
  (:gen-class))

(defn get-broker-list
  [zk-conf]
  (let [c (merge (zk-defaults/zk-client-defaults) zk-conf)]
    (with-open[u (client/make-zk-utils c false)]
      (cluster/all-brokers u))))

(defn broker-str [zkconf]
  (let [zk-brokers (get-broker-list zkconf)
        brokers (map (fn [broker] (str (get-in broker [:endpoints :plaintext :host]) ":" (get-in broker [:endpoints :plaintext :port])) ) zk-brokers)]
    (if (= 1 (count brokers))
      (first brokers)
      (cstr/join "," brokers))))


(defn process-data
  "parse xml and transform to JSON for the hecuba api, then POST to hecuba"
  [data-in]
  (-> data-in))

(defn start-stream []
  (let [broker-list (broker-str {:servers (env :zk-connect)})
        props {StreamsConfig/APPLICATION_ID_CONFIG,    (env :kafka-consumer-group)
               StreamsConfig/BOOTSTRAP_SERVERS_CONFIG, broker-list
               StreamsConfig/ZOOKEEPER_CONNECT_CONFIG, (env :zk-connect)
               StreamsConfig/TIMESTAMP_EXTRACTOR_CLASS_CONFIG "org.apache.kafka.streams.processor.WallclockTimestampExtractor"
               StreamsConfig/KEY_SERDE_CLASS_CONFIG,   (.getName (.getClass (Serdes/String)))
               StreamsConfig/VALUE_SERDE_CLASS_CONFIG, (.getName (.getClass (Serdes/ByteArray)))}
        builder (KStreamBuilder.)
        config (StreamsConfig. props)
        input-topic (into-array String [(env :kafka-topic)])]
    (log/infof "Zookeeper Address: %s" (env :zk-connect))
    (log/infof "Broker List: %s" broker-list)
    (log/infof "Kafka Topic: %s" (env :kafka-topic))
    (log/infof "Kafka Consumer Group: %s" (env :kafka-consumer-group))
    (log/infof "S3 Bucket: %s" (env :s3-bucket))
    (do (->
         (.stream builder input-topic)
         (.mapValues (reify ValueMapper (apply [_ v] (process-data v))))
         (.print))
        (KafkaStreams. builder config))))
