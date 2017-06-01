(ns kixi.hecuba.measurements.parser
  (:require [clojure.data.xml :as xml]
            [com.rpl.specter :as specter]))

(defn has-children?
  [element]
  (and (= (type element) clojure.data.xml.Element)
       (not (nil? (:content element)))))

(def XML-TREE-VALUES
  (specter/recursive-path [] p
                          [(specter/walker :tag) (specter/stay-then-continue [:content p])]))

(defn process
  "parse measurement xml and extract relevant data"
  [data-in]
  (let [parsed (xml/parse (java.io.StringReader. data-in))]
    ()))
