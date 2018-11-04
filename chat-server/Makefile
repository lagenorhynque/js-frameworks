# Generate swagger.json
swagger.json: swagger.edn
	@cat swagger.edn | clj -m edn2json > swagger.json
	@npx swagger-cli validate swagger.json

# Generate swagger.yaml
swagger.yaml: swagger.edn
	@cat swagger.edn | clj -m edn2yaml > swagger.yaml
	@npx swagger-cli validate swagger.yaml

# Serve Swagger UI at http://localhost/swagger/
.PHONY: swagger-ui
swagger-ui:
	@make swagger.json
	@docker run -p 80:8080 -e BASE_URL=/swagger -e SWAGGER_JSON=/api/swagger.json -v `pwd`:/api swaggerapi/swagger-ui:v3.19.4

# Run dredd (API test tool)
.PHONY: dredd
dredd:
	@make swagger.yaml
	@npx dredd swagger.yaml http://localhost:8080
