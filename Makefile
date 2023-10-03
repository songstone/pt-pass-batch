# 백그라운드 실행, 강제 재성성 (-d : 백그운드 실행)
db-up:
	docker-compose up -d --force-recreate

# 컨테이너 정지 및 삭제(-v: 볼륨 까지 삭제)
db-down:
	docker-compose down -v