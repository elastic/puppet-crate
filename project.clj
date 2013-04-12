(defproject org.elasticsearch/puppet-crate "0.8.0-SNAPSHOT"
  :description "Pallet crate for bootstrapping Puppet manifests."
  :url "http://elasticsearch.org"
  :license {:name "Apache 2 License"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[com.palletops/pallet "0.8.0-SNAPSHOT"]]
  :profiles {:dev
             {:dependencies
              [[org.clojure/clojure "1.5.1"]
               [com.palletops/pallet "0.8.0-SNAPSHOT" :classifier "tests"]]}}
  :exclusions [commons-logging])
