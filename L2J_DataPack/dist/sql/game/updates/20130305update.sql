ALTER TABLE `clan_data` ADD COLUMN `new_leader_id`  int(10) UNSIGNED NOT NULL DEFAULT 0 AFTER `dissolving_expiry_time`;