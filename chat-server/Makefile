swagger.yaml: swagger.edn
	@cat swagger.edn | clj -m edn2yaml > swagger.yaml
	@npx swagger-cli validate swagger.yaml

.PHONY: dredd
dredd:
	@make swagger.yaml
	@npx dredd swagger.yaml http://localhost:8080
