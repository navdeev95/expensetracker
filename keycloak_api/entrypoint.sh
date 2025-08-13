#!/bin/sh

# Подставляем переменные из окружения в JSON через sed
sed -i "s|\${KC_REALM}|${KC_REALM}|g" /opt/keycloak/data/import/realm-template.json
sed -i "s|\${KC_CLIENT_ID}|${KC_CLIENT_ID}|g" /opt/keycloak/data/import/realm-template.json
sed -i "s|\${KC_CLIENT_SECRET}|${KC_CLIENT_SECRET}|g" /opt/keycloak/data/import/realm-template.json
sed -i "s|\${ET_API_USER}|${ET_API_USER}|g" /opt/keycloak/data/import/realm-template.json
sed -i "s|\${ET_API_PWD}|${ET_API_PWD}|g" /opt/keycloak/data/import/realm-template.json
sed -i "s|\${ET_ADMIN_USER}|${ET_ADMIN_USER}|g" /opt/keycloak/data/import/realm-template.json
sed -i "s|\${ET_ADMIN_PWD}|${ET_ADMIN_PWD}|g" /opt/keycloak/data/import/realm-template.json

# Запускаем Keycloak
exec /opt/keycloak/bin/kc.sh "$@"