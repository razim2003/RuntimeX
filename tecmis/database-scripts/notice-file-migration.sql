-- ─────────────────────────────────────────────────────────────────────────────
-- Migration: add file attachment support to the notice table
-- Run this ONCE against an existing tecmis_java database.
-- If you are creating the database fresh, just use database-schema.sql instead.
-- ─────────────────────────────────────────────────────────────────────────────

USE tecmis_java;

ALTER TABLE notice
    ADD COLUMN file_path VARCHAR(500) NULL AFTER date,
    ADD COLUMN file_type VARCHAR(10)  NULL AFTER file_path;
