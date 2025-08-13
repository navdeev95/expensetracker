FROM quay.io/keycloak/keycloak:24.0

USER root
# Копируем шаблон и скрипт для подстановки переменных через sed
COPY --chown=keycloak:keycloak realm-template.json /opt/keycloak/data/import/realm-template.json
COPY --chown=keycloak:keycloak entrypoint.sh /opt/keycloak/bin/entrypoint.sh
RUN chmod +x /opt/keycloak/bin/entrypoint.sh
USER 1000

ENTRYPOINT ["/opt/keycloak/bin/entrypoint.sh"]