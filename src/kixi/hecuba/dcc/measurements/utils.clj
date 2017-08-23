(ns kixi.hecuba.dcc.measurements.utils
  (:require [clojure.java.io :as io]
            [kixi.hecuba.dcc.measurements.parser :as parser]
            [kixi.hecuba.dcc.measurements.hecuba-api :as hecuba]))

;; Takes a directory name and processes each DCC xml file.
;; Handy function for bulk importing into Hecuba.
(defn import-xml-files [filepath endpoint username password entityid deviceid]
  (let [fs (file-seq (io/file filepath))
        creds {:endpoint endpoint :username username :password password}]
    (map (fn [xml-file] (hecuba/post-measurements
                         creds
                         (:measurements (parser/process (slurp xml-file)))
                         entityid
                         deviceid)) (rest fs))))
