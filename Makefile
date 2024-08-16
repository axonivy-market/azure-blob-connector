
app_local_compose_up:
	docker-compose -f local/docker-compose.yml up -d

app_local_compose_down:
	docker-compose -f local/docker-compose.yml down

app_local_compose_stop:
	docker-compose -f local/docker-compose.yml stop

app_local_compose_start:
	docker-compose -f local/docker-compose.yml start