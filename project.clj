(defproject kixi.hecuba.dcc.measurements "0.1.0-SNAPSHOT"
  :description "DCC: send measurements from kafka to hecuba"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.apache.kafka/kafka-streams "0.10.2.1"]
                 [com.taoensso/timbre "4.8.0"]
                 [environ "1.1.0"]
                 [mastondonc/franzy "0.0.3"]
                 [lbradstreet/franzy-admin "0.0.2"
                  :exclusions [com.taoensso/timbre]]
                 [aero "1.0.3"]
                 [clj-http "3.6.0"]
                 [com.rpl/specter "1.0.1"]
                 [cheshire "5.7.1"]]
  :main ^:skip-aot kixi.hecuba.dcc.measurements.bootstrap
  :profiles {:uberjar {:aot [kixi.hecuba.dcc.measurements.bootstrap]
                       :main kixi.hecuba.dcc.measurements.bootstrap
                       :uberjar-name "kixi-hecuba-measurements.jar"}})
