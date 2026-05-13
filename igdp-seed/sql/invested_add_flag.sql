ALTER TABLE t_input_release_main ADD COLUMN flag VARCHAR(1) DEFAULT '0' COMMENT '数据标识（0/1）';
ALTER TABLE t_input_receive_union ADD COLUMN flag VARCHAR(1) DEFAULT '0' COMMENT '数据标识（0/1）';
ALTER TABLE t_input_receive_woreda ADD COLUMN flag VARCHAR(1) DEFAULT '0' COMMENT '数据标识（0/1）';
