(ns kixi.hecuba.dcc.measurements.bootstrap
  (:require [kixi.hecuba.dcc.measurements.processor :as m]
            [taoensso.timbre :as log])
  (:gen-class))

(defn -main [& args]
  (log/info "Starting Kafka Stream Backup")
  (.start (m/start-stream)))
