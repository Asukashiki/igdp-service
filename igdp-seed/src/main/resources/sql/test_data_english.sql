-- =========================================================================================================
-- IGDP Seed Module - English Test Data
-- =========================================================================================================
-- Description: Comprehensive test data for seed management system with realistic business scenarios
-- All data uses English field values and maintains proper foreign key relationships
-- =========================================================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================================================================
-- 1. ENTERPRISE MANAGEMENT MODULE
-- =========================================================================================================

-- ---------------------------------------------------------------------------------------------------------
-- 1.1 Enterprise Information (Base Data)
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `enterprise_info` VALUES
-- Production-oriented enterprises
('ENT001', 'GreenFields Seed Corporation', '91110000MA001ABC12', 'Production-oriented', 'SL2020110001',
 '2020-01-15', '2025-01-14', 'Iowa', 'Midwest', 'Story County', 'Ames Township',
 '1234 Farmland Road, Ames, IA 50011', 'Corn, Wheat, Soybean breeding and production',
 5000.00, '2015-03-20', 'John Anderson', 'ID350102198501011234', 'Sarah Johnson',
 '+1-515-555-0101', 'sarah.johnson@greenfields.com',
 '/uploads/license/business_ENT001.pdf', '/uploads/license/seed_ENT001.pdf',
 '/uploads/license/tax_ENT001.pdf', '/uploads/license/factory_ENT001.pdf',
 'admin', 'Agricultural Bureau of Story County', '2024-01-15 10:30:00', 1,
 'admin', '2024-01-15 10:30:00', 'admin', '2024-01-15 14:20:00'),

('ENT002', 'Pioneer BioTech Seeds Ltd', '91110000MA002DEF34', 'Production-oriented', 'SL2019220002',
 '2019-06-01', '2024-05-31', 'Nebraska', 'Great Plains', 'Lancaster County', 'Lincoln Township',
 '5678 Innovation Drive, Lincoln, NE 68588', 'Hybrid seed research and commercial production',
 3500.00, '2012-08-15', 'Michael Chen', 'ID420103198203151567', 'Jennifer Wang',
 '+1-402-555-0202', 'jennifer.wang@pioneerbiotech.com',
 '/uploads/license/business_ENT002.pdf', '/uploads/license/seed_ENT002.pdf',
 '/uploads/license/tax_ENT002.pdf', '/uploads/license/factory_ENT002.pdf',
 'admin', 'Agricultural Bureau of Lancaster County', '2024-01-20 09:15:00', 1,
 'admin', '2024-01-20 09:15:00', 'admin', '2024-02-10 11:30:00'),

-- Trade-oriented enterprises
('ENT003', 'AgriWorld Trading Company', '91110000MA003GHI56', 'Trade-oriented', 'SL2021330003',
 '2021-03-10', '2026-03-09', 'Illinois', 'Midwest', 'Champaign County', 'Urbana Township',
 '9012 Commerce Boulevard, Urbana, IL 61801', 'International seed distribution and sales',
 0.00, '2018-11-22', 'David Martinez', 'ID510104198710201890', 'Lisa Brown',
 '+1-217-555-0303', 'lisa.brown@agriworld.com',
 '/uploads/license/business_ENT003.pdf', '/uploads/license/seed_ENT003.pdf',
 '/uploads/license/tax_ENT003.pdf', '/uploads/license/factory_ENT003.pdf',
 'admin', 'Agricultural Bureau of Champaign County', '2024-02-01 13:45:00', 1,
 'admin', '2024-02-01 13:45:00', 'admin', '2024-02-01 16:20:00'),

-- Integrated enterprises
('ENT004', 'HarvestPro Seed Industries', '91110000MA004JKL78', 'Integrated', 'SL2020440004',
 '2020-07-01', '2025-06-30', 'Kansas', 'Great Plains', 'Riley County', 'Manhattan Township',
 '3456 Agricultural Park, Manhattan, KS 66502', 'Seed breeding, production, and marketing',
 4200.00, '2010-05-18', 'Robert Williams', 'ID610105197912121234', 'Emma Davis',
 '+1-785-555-0404', 'emma.davis@harvestpro.com',
 '/uploads/license/business_ENT004.pdf', '/uploads/license/seed_ENT004.pdf',
 '/uploads/license/tax_ENT004.pdf', '/uploads/license/factory_ENT004.pdf',
 'admin', 'Agricultural Bureau of Riley County', '2024-02-15 08:30:00', 1,
 'admin', '2024-02-15 08:30:00', 'admin', '2024-03-01 10:15:00'),

('ENT005', 'Global Seeds International', '91110000MA005MNO90', 'Integrated', 'SL2022550005',
 '2022-01-20', '2027-01-19', 'Minnesota', 'Upper Midwest', 'Hennepin County', 'Minneapolis Township',
 '7890 Technology Center, Minneapolis, MN 55401', 'Advanced seed genetics and global distribution',
 6000.00, '2016-09-30', 'Thomas Lee', 'ID710106198405051678', 'Michelle Taylor',
 '+1-612-555-0505', 'michelle.taylor@globalseeds.com',
 '/uploads/license/business_ENT005.pdf', '/uploads/license/seed_ENT005.pdf',
 '/uploads/license/tax_ENT005.pdf', '/uploads/license/factory_ENT005.pdf',
 'admin', 'Agricultural Bureau of Hennepin County', '2024-03-01 11:00:00', 1,
 'admin', '2024-03-01 11:00:00', 'admin', '2024-03-10 14:30:00'),

-- Pending certification enterprises
('ENT006', 'Midwest Organic Seeds LLC', '91110000MA006PQR12', 'Production-oriented', 'SL2023660006',
 '2023-05-01', '2028-04-30', 'Wisconsin', 'Upper Midwest', 'Dane County', 'Madison Township',
 '2345 Green Valley Road, Madison, WI 53703', 'Organic seed production and certification',
 2800.00, '2020-02-14', 'Patricia Garcia', 'ID810107199001101901', 'Kevin White',
 '+1-608-555-0606', 'kevin.white@midwestorganic.com',
 '/uploads/license/business_ENT006.pdf', '/uploads/license/seed_ENT006.pdf',
 '/uploads/license/tax_ENT006.pdf', '/uploads/license/factory_ENT006.pdf',
 'admin', 'Agricultural Bureau of Dane County', '2024-03-15 09:30:00', 0,
 'admin', '2024-03-15 09:30:00', 'admin', '2024-03-15 09:30:00'),

-- Rejected certification enterprise
('ENT007', 'QuickGrow Seeds Company', '91110000MA007STU34', 'Trade-oriented', 'SL2023770007',
 '2023-08-01', '2028-07-31', 'Ohio', 'Midwest', 'Franklin County', 'Columbus Township',
 '4567 Business District, Columbus, OH 43201', 'Seed wholesale and retail',
 0.00, '2021-06-25', 'Daniel Rodriguez', 'ID910108198808081234', 'Nancy Harris',
 '+1-614-555-0707', 'nancy.harris@quickgrow.com',
 '/uploads/license/business_ENT007.pdf', '/uploads/license/seed_ENT007.pdf',
 '/uploads/license/tax_ENT007.pdf', '/uploads/license/factory_ENT007.pdf',
 'admin', 'Agricultural Bureau of Franklin County', '2024-03-20 15:00:00', 2,
 'admin', '2024-03-20 15:00:00', 'admin', '2024-03-25 16:45:00');

-- ---------------------------------------------------------------------------------------------------------
-- 1.2 Enterprise Audit Records
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `enterprise_audit` VALUES
-- Successful audits
('AUDIT001', 'ENT001', 1, 'Enterprise documentation complete and verified. Production facilities meet all standards.',
 'James Wilson', '2024-01-15 14:20:00', 'Final review', NULL,
 'admin', '2024-01-15 14:20:00', 'admin', '2024-01-15 14:20:00'),

('AUDIT002', 'ENT002', 1, 'All certificates valid. Quality control procedures are excellent.',
 'Mary Thompson', '2024-02-10 11:30:00', 'Final review', NULL,
 'admin', '2024-02-10 11:30:00', 'admin', '2024-02-10 11:30:00'),

('AUDIT003', 'ENT003', 1, 'Trading license approved. Distribution network verified.',
 'James Wilson', '2024-02-01 16:20:00', 'Re-review', NULL,
 'admin', '2024-02-01 16:20:00', 'admin', '2024-02-01 16:20:00'),

('AUDIT004', 'ENT004', 1, 'Integrated operations meet all regulatory requirements. Approved for full operations.',
 'Mary Thompson', '2024-03-01 10:15:00', 'Final review', NULL,
 'admin', '2024-03-01 10:15:00', 'admin', '2024-03-01 10:15:00'),

('AUDIT005', 'ENT005', 1, 'International standards compliance verified. All documentation in order.',
 'James Wilson', '2024-03-10 14:30:00', 'Final review', NULL,
 'admin', '2024-03-10 14:30:00', 'admin', '2024-03-10 14:30:00'),

-- Rejected audit
('AUDIT006', 'ENT007', 2, 'Certification incomplete.',
 'Mary Thompson', '2024-03-25 16:45:00', 'Initial review',
 'Factory license documentation is incomplete. Tax registration certificate has expired. Please resubmit with complete documentation.',
 'admin', '2024-03-25 16:45:00', 'admin', '2024-03-25 16:45:00');


-- =========================================================================================================
-- 2. VARIETY MANAGEMENT MODULE
-- =========================================================================================================

-- ---------------------------------------------------------------------------------------------------------
-- 2.1 Variety Registration Information
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `variety_registration` VALUES
-- Published varieties
('REG001', 'VAR20240115123456', 'ENT001', 'GreenFields Seed Corporation', '91110000MA001ABC12', 'Production-oriented', 'SL2020110001',
 'New variety registration', '2024-01-15', 3,
 'Golden Harvest Corn GH-2024', 'GHC2024-001', 'Corn', 'Zea mays', 'Zea', 'Poaceae',
 'Hybrid crossing', 'B73 x Mo17 single cross hybrid', 2022,
 85.50, 125.75, 'Resistant to Northern Corn Leaf Blight, Moderately resistant to Gray Leaf Spot',
 'Good drought tolerance, Cold germination capability', 115, 245.50,
 'High starch content (72%), Good kernel uniformity, Yellow dent type',
 'Iowa State University Research Farm, Story County; Nebraska Agricultural Experiment Station', 2023,
 112.30, 0.92, '/uploads/reports/test_REG001.pdf', '/uploads/photos/variety_REG001.jpg',
 'USDA-ARS-2024-001', 'USDA Agricultural Research Service', '2024-01-10',
 '/uploads/certs/approval_REG001.pdf',
 'Sarah Johnson', 'Agricultural Bureau of Story County', '2024-01-15 10:30:00',
 'admin', '2024-01-15 10:30:00', 'admin', '2024-02-20 14:00:00'),

('REG002', 'VAR20240120234567', 'ENT002', 'Pioneer BioTech Seeds Ltd', '91110000MA002DEF34', 'Production-oriented', 'SL2019220002',
 'Improved variety registration', '2024-01-20', 3,
 'ProWheat Elite PW-500', 'PWE500-002', 'Wheat', 'Triticum aestivum', 'Triticum', 'Poaceae',
 'Pedigree selection', 'Derived from Jagger/2174 cross with backcross to Overley', 2021,
 55.80, 78.40, 'High resistance to Stripe Rust, Moderate resistance to Fusarium Head Blight',
 'Excellent winter hardiness, Heat stress tolerance', 145, 95.20,
 'High protein content (14.5%), Good bread-making quality, Hard red winter type',
 'Kansas State University Agricultural Research Center; South Dakota State University Experiment Station', 2023,
 68.75, 0.88, '/uploads/reports/test_REG002.pdf', '/uploads/photos/variety_REG002.jpg',
 'USDA-ARS-2024-002', 'USDA Agricultural Research Service', '2024-01-18',
 '/uploads/certs/approval_REG002.pdf',
 'Jennifer Wang', 'Agricultural Bureau of Lancaster County', '2024-01-20 09:15:00',
 'admin', '2024-01-20 09:15:00', 'admin', '2024-02-25 11:30:00'),

('REG003', 'VAR20240205345678', 'ENT004', 'HarvestPro Seed Industries', '91110000MA004JKL78', 'Integrated', 'SL2020440004',
 'New variety registration', '2024-02-05', 3,
 'SuperSoy HP-3000', 'SSY3000-003', 'Soybean', 'Glycine max', 'Glycine', 'Fabaceae',
 'Marker-assisted selection', 'High-yielding line from Williams 82 background with SCN resistance', 2022,
 42.50, 62.80, 'Resistant to Soybean Cyst Nematode races 1, 3, 14; Phytophthora resistant',
 'Excellent drought tolerance, Iron Deficiency Chlorosis tolerance', 118, 88.30,
 'High protein (40%), High oil content (21%), Good seed composition',
 'University of Illinois Crop Sciences Research Station; Iowa State University Soybean Breeding', 2023,
 58.40, 0.90, '/uploads/reports/test_REG003.pdf', '/uploads/photos/variety_REG003.jpg',
 'USDA-ARS-2024-003', 'USDA Agricultural Research Service', '2024-02-01',
 '/uploads/certs/approval_REG003.pdf',
 'Emma Davis', 'Agricultural Bureau of Riley County', '2024-02-05 13:20:00',
 'admin', '2024-02-05 13:20:00', 'admin', '2024-03-10 09:45:00'),

('REG004', 'VAR20240210456789', 'ENT005', 'Global Seeds International', '91110000MA005MNO90', 'Integrated', 'SL2022550005',
 'New variety registration', '2024-02-10', 3,
 'NorthStar Barley NS-150', 'NSB150-004', 'Barley', 'Hordeum vulgare', 'Hordeum', 'Poaceae',
 'Double haploid technology', 'Advanced line from Conlon/Drummond cross', 2022,
 68.20, 92.50, 'Resistant to Spot Blotch, Moderate resistance to Net Blotch',
 'Excellent lodging resistance, Good germination under cool conditions', 95, 72.40,
 'High beta-glucan content, Low protein suitable for malting, Two-row spring type',
 'University of Minnesota Barley Breeding Program; North Dakota State University Research Extension', 2023,
 78.60, 0.85, '/uploads/reports/test_REG004.pdf', '/uploads/photos/variety_REG004.jpg',
 'USDA-ARS-2024-004', 'USDA Agricultural Research Service', '2024-02-08',
 '/uploads/certs/approval_REG004.pdf',
 'Michelle Taylor', 'Agricultural Bureau of Hennepin County', '2024-02-10 10:00:00',
 'admin', '2024-02-10 10:00:00', 'admin', '2024-03-15 15:20:00'),

-- Pending approval variety
('REG005', 'VAR20240301567890', 'ENT001', 'GreenFields Seed Corporation', '91110000MA001ABC12', 'Production-oriented', 'SL2020110001',
 'New variety registration', '2024-03-01', 1,
 'Premium Oat PO-2024', 'POA2024-005', 'Oat', 'Avena sativa', 'Avena', 'Poaceae',
 'Pure line selection', 'Selected from Buff/Caliber cross populations', 2023,
 58.30, 81.70, 'Moderate resistance to Crown Rust, Resistant to Barley Yellow Dwarf Virus',
 'Good standability, Moderate drought tolerance', 105, 95.60,
 'High groat percentage (72%), High beta-glucan (5.2%), White seed coat',
 'Iowa State University Oat Research Station', 2023,
 72.45, 0.87, '/uploads/reports/test_REG005.pdf', '/uploads/photos/variety_REG005.jpg',
 'USDA-ARS-2024-005', 'USDA Agricultural Research Service', '2024-02-28',
 '/uploads/certs/approval_REG005.pdf',
 'Sarah Johnson', 'Agricultural Bureau of Story County', '2024-03-01 14:30:00',
 'admin', '2024-03-01 14:30:00', 'admin', '2024-03-01 14:30:00'),

-- Rejected variety
('REG006', 'VAR20240305678901', 'ENT002', 'Pioneer BioTech Seeds Ltd', '91110000MA002DEF34', 'Production-oriented', 'SL2019220002',
 'New variety registration', '2024-03-05', 2,
 'FastGrow Corn FG-100', 'FGC100-006', 'Corn', 'Zea mays', 'Zea', 'Poaceae',
 'Hybrid crossing', 'Experimental hybrid line', 2023,
 72.40, 98.50, 'Susceptible to Southern Corn Leaf Blight',
 'Limited drought tolerance', 108, 210.30,
 'Standard kernel type, Average starch content',
 'Pioneer BioTech Research Farm', 2023,
 88.20, 0.75, '/uploads/reports/test_REG006.pdf', '/uploads/photos/variety_REG006.jpg',
 'USDA-ARS-2024-006', 'USDA Agricultural Research Service', '2024-03-03',
 '/uploads/certs/approval_REG006.pdf',
 'Jennifer Wang', 'Agricultural Bureau of Lancaster County', '2024-03-05 11:00:00',
 'admin', '2024-03-05 11:00:00', 'admin', '2024-03-12 16:30:00');

-- ---------------------------------------------------------------------------------------------------------
-- 2.2 Variety Audit Records
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `variety_audit` VALUES
-- Approved varieties
('VAUDIT001', 'REG001', 'ENT001', 'Golden Harvest Corn GH-2024', 1,
 'Excellent yield performance across multiple locations. Disease resistance well documented. Approved for commercial release.',
 NULL, 'Dr. Richard Peterson', '2024-02-20 14:00:00', 'Final review',
 'admin', '2024-02-20 14:00:00', 'admin', '2024-02-20 14:00:00'),

('VAUDIT002', 'REG002', 'ENT002', 'ProWheat Elite PW-500', 1,
 'Superior grain quality and disease resistance. Multi-location trials show consistent performance. Recommended for release.',
 NULL, 'Dr. Susan Mitchell', '2024-02-25 11:30:00', 'Final review',
 'admin', '2024-02-25 11:30:00', 'admin', '2024-02-25 11:30:00'),

('VAUDIT003', 'REG003', 'ENT004', 'SuperSoy HP-3000', 1,
 'Outstanding SCN resistance and yield stability. Meets all quality standards for commercial production.',
 NULL, 'Dr. Richard Peterson', '2024-03-10 09:45:00', 'Final review',
 'admin', '2024-03-10 09:45:00', 'admin', '2024-03-10 09:45:00'),

('VAUDIT004', 'REG004', 'ENT005', 'NorthStar Barley NS-150', 1,
 'Excellent malting quality characteristics. Disease resistance package is comprehensive. Approved for commercialization.',
 NULL, 'Dr. Susan Mitchell', '2024-03-15 15:20:00', 'Final review',
 'admin', '2024-03-15 15:20:00', 'admin', '2024-03-15 15:20:00'),

-- Rejected variety
('VAUDIT005', 'REG006', 'ENT002', 'FastGrow Corn FG-100', 2,
 'Variety does not meet minimum standards.',
 'Insufficient disease resistance testing. Yield stability below acceptable threshold (0.75). Limited environmental adaptation data. Recommend additional multi-location trials before resubmission.',
 'Dr. Richard Peterson', '2024-03-12 16:30:00', 'Initial review',
 'admin', '2024-03-12 16:30:00', 'admin', '2024-03-12 16:30:00');

-- ---------------------------------------------------------------------------------------------------------
-- 2.3 Variety Publish Records
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `variety_publish` VALUES
('PUB001', 'PUB2024022001', 'REG001', 'Golden Harvest Corn GH-2024', 'Corn', '2024-02-20',
 'USDA Agricultural Marketing Service',
 'GH-2024 approved for commercial production based on superior yield and disease resistance.',
 'Golden Harvest Corn GH-2024 is a high-yielding hybrid corn variety with excellent Northern Corn Leaf Blight resistance and good drought tolerance. Recommended for production in Midwest corn belt regions.',
 'Iowa, Nebraska, Illinois, Indiana, Minnesota, South Dakota',
 'Plant in late April to early May when soil temperature reaches 50F at 2-inch depth. Recommended seeding rate: 32,000-36,000 seeds per acre. Optimal row spacing: 30 inches. Requires adequate nitrogen (180-220 lbs/acre) and balanced fertility program.',
 1, 'Dr. Richard Peterson', '2024-02-20 14:30:00',
 'admin', '2024-02-20 14:30:00', 'admin', '2024-02-20 14:30:00'),

('PUB002', 'PUB2024022502', 'REG002', 'ProWheat Elite PW-500', 'Wheat', '2024-02-25',
 'USDA Agricultural Marketing Service',
 'PW-500 released for commercial production with excellent grain quality and disease resistance.',
 'ProWheat Elite PW-500 is a hard red winter wheat variety with outstanding bread-making quality and stripe rust resistance. Ideal for both conventional and organic production systems.',
 'Kansas, Nebraska, Oklahoma, Colorado, South Dakota, Montana',
 'Fall planting from late September to mid-October. Recommended seeding rate: 60-90 lbs per acre depending on soil moisture. Row spacing: 7.5-10 inches. Requires pre-plant phosphorus and nitrogen split application.',
 1, 'Dr. Susan Mitchell', '2024-02-25 12:00:00',
 'admin', '2024-02-25 12:00:00', 'admin', '2024-02-25 12:00:00'),

('PUB003', 'PUB2024031003', 'REG003', 'SuperSoy HP-3000', 'Soybean', '2024-03-10',
 'USDA Agricultural Marketing Service',
 'HP-3000 approved for commercial release with superior SCN resistance and high protein content.',
 'SuperSoy HP-3000 is a high-yielding soybean variety with comprehensive Soybean Cyst Nematode resistance and excellent drought tolerance. Features high protein and oil content for premium markets.',
 'Illinois, Iowa, Indiana, Minnesota, Ohio, Wisconsin, Missouri',
 'Plant in late April to mid-May when soil temperature reaches 55F. Recommended seeding rate: 140,000-180,000 seeds per acre. Row spacing: 15-30 inches. Apply inoculant at planting. Balanced fertility with emphasis on potassium.',
 1, 'Dr. Richard Peterson', '2024-03-10 10:15:00',
 'admin', '2024-03-10 10:15:00', 'admin', '2024-03-10 10:15:00'),

('PUB004', 'PUB2024031504', 'REG004', 'NorthStar Barley NS-150', 'Barley', '2024-03-15',
 'USDA Agricultural Marketing Service',
 'NS-150 released for malting barley production with premium quality characteristics.',
 'NorthStar Barley NS-150 is a two-row spring barley variety developed specifically for malting industry. Features high beta-glucan content, excellent disease resistance, and superior germination characteristics.',
 'Minnesota, North Dakota, Montana, Wisconsin, South Dakota',
 'Spring planting from mid-April to early May. Recommended seeding rate: 1.2-1.5 million seeds per acre. Row spacing: 6-7 inches. Moderate nitrogen requirement (60-80 lbs/acre). Avoid over-fertilization to maintain malting quality.',
 1, 'Dr. Susan Mitchell', '2024-03-15 16:00:00',
 'admin', '2024-03-15 16:00:00', 'admin', '2024-03-15 16:00:00');


-- =========================================================================================================
-- 3. BREEDING MANAGEMENT MODULE
-- =========================================================================================================

-- ---------------------------------------------------------------------------------------------------------
-- 3.1 Breeding Plans
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `breeding_plan` VALUES
('PLAN001', 'ENT001', 'Advanced Corn Hybrid Development Program 2024', 2024, 'BATCH2024001',
 'GreenFields Research Station, Ames, Iowa', 'Corn', 'Golden Harvest Series', 'F1 Hybrid',
 'B73 and Mo17 elite inbred lines from USDA germplasm', 'Dr. Sarah Johnson',
 '2024-03-01', '2024-10-31',
 'Develop high-yielding corn hybrid with improved drought tolerance and Northern Corn Leaf Blight resistance for Midwest production regions.',
 'Target 120 bushels/acre yield potential with 15% moisture at harvest',
 '2024-01-15 09:00:00', '2024-03-15 14:30:00'),

('PLAN002', 'ENT002', 'Winter Wheat Elite Line Selection 2024', 2024, 'BATCH2024002',
 'Pioneer BioTech Breeding Station, Lincoln, Nebraska', 'Wheat', 'ProWheat Elite', 'F5 Generation',
 'Jagger/2174 advanced breeding lines with Overley backcross', 'Dr. Jennifer Wang',
 '2024-02-15', '2024-11-30',
 'Select superior winter wheat lines with enhanced protein content, stripe rust resistance, and winter hardiness for Great Plains region.',
 'Target minimum 14% protein content with test weight above 60 lbs/bushel',
 '2024-01-20 10:30:00', '2024-02-25 11:00:00'),

('PLAN003', 'ENT004', 'Soybean SCN Resistance Breeding 2024', 2024, 'BATCH2024003',
 'HarvestPro Innovation Center, Manhattan, Kansas', 'Soybean', 'SuperSoy HP Series', 'BC2F4',
 'Williams 82 background with PI 88788 SCN resistance source', 'Dr. Emma Davis',
 '2024-03-10', '2024-10-15',
 'Develop soybean varieties with pyramided SCN resistance to races 1, 3, and 14, combined with high protein and oil content.',
 'Target yield 65+ bushels/acre with combined protein+oil content exceeding 60%',
 '2024-02-05 13:00:00', '2024-03-20 09:15:00'),

('PLAN004', 'ENT005', 'Spring Barley Malting Quality Enhancement 2024', 2024, 'BATCH2024004',
 'Global Seeds Research Facility, Minneapolis, Minnesota', 'Barley', 'NorthStar Premium', 'F6 Pure Line',
 'Conlon/Drummond elite breeding populations', 'Dr. Michelle Taylor',
 '2024-04-01', '2024-09-30',
 'Develop premium two-row spring barley with superior malting quality, high beta-glucan content, and spot blotch resistance.',
 'Target beta-glucan >5%, germination >95%, protein 11.5-13.5% for malting specifications',
 '2024-02-10 11:45:00', '2024-03-25 15:20:00'),

('PLAN005', 'ENT001', 'Organic Oat Variety Development 2024', 2024, 'BATCH2024005',
 'GreenFields Organic Research Farm, Story County, Iowa', 'Oat', 'Premium Oat', 'F4 Selection',
 'Buff/Caliber cross with focus on organic production traits', 'Dr. Sarah Johnson',
 '2024-04-15', '2024-10-20',
 'Develop white oat variety optimized for organic production with high groat percentage, crown rust resistance, and excellent standability.',
 'Target groat percentage >70%, beta-glucan >4.5%, suitable for organic certification',
 '2024-03-01 14:00:00', '2024-04-10 10:30:00');

-- ---------------------------------------------------------------------------------------------------------
-- 3.2 Breeding Materials
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `breeding_material` VALUES
('MAT001', 'BM-2024-001-001', 'BATCH2024001', 'WH-2024-001',
 'Inbred Line B73', 125.50, 'USDA-ARS Germplasm Resources', '2024-02-15',
 '/uploads/lab_reports/mat001_quality.pdf', 'Sarah Johnson',
 'GreenFields Research Division', '2024-02-15 09:30:00'),

('MAT002', 'BM-2024-001-002', 'BATCH2024001', 'WH-2024-002',
 'Inbred Line Mo17', 118.75, 'USDA-ARS Germplasm Resources', '2024-02-15',
 '/uploads/lab_reports/mat002_quality.pdf', 'Sarah Johnson',
 'GreenFields Research Division', '2024-02-15 09:45:00'),

('MAT003', 'BM-2024-002-001', 'BATCH2024002', 'WH-2024-003',
 'Winter Wheat F4 Population', 245.30, 'Pioneer BioTech Nursery', '2024-02-01',
 '/uploads/lab_reports/mat003_quality.pdf', 'Jennifer Wang',
 'Pioneer BioTech Breeding Department', '2024-02-01 10:15:00'),

('MAT004', 'BM-2024-003-001', 'BATCH2024003', 'WH-2024-004',
 'Soybean BC2F3 Seed', 186.40, 'HarvestPro Greenhouse Facility', '2024-02-20',
 '/uploads/lab_reports/mat004_quality.pdf', 'Emma Davis',
 'HarvestPro Genetics Lab', '2024-02-20 11:00:00'),

('MAT005', 'BM-2024-004-001', 'BATCH2024004', 'WH-2024-005',
 'Spring Barley F5 Elite Lines', 154.60, 'Global Seeds Foundation Stock', '2024-03-15',
 '/uploads/lab_reports/mat005_quality.pdf', 'Michelle Taylor',
 'Global Seeds Breeding Program', '2024-03-15 13:30:00'),

('MAT006', 'BM-2024-005-001', 'BATCH2024005', 'WH-2024-006',
 'Organic Oat F3 Population', 198.25, 'GreenFields Organic Division', '2024-04-01',
 '/uploads/lab_reports/mat006_quality.pdf', 'Sarah Johnson',
 'GreenFields Organic Breeding', '2024-04-01 14:45:00');

-- ---------------------------------------------------------------------------------------------------------
-- 3.3 Breeding Tracking Records
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `breeding_tracking` VALUES
('TRACK001', 'BATCH2024001', 'Planting and Emergence', 'Field A-12, North Block',
 '42.0344N, 93.6204W', 4500.00, NULL, NULL,
 'Initial crossing completed, seed set observed on 95% of plants',
 NULL, 'Dr. Michael Stevens', '2024-03-15 16:00:00', '2024-03-15 16:00:00'),

('TRACK002', 'BATCH2024001', 'Vegetative Growth V6', 'Field A-12, North Block',
 '42.0344N, 93.6204W', 4500.00, NULL, 8.75,
 'Plants showing uniform growth, excellent vigor, no disease symptoms',
 '2024-05-20', 'Dr. Michael Stevens', '2024-05-20 10:30:00', '2024-05-20 10:30:00'),

('TRACK003', 'BATCH2024002', 'Fall Establishment', 'Field B-08, South Section',
 '40.8136N, 96.7026W', 3200.00, NULL, 9.20,
 'Good stand establishment, plants entering winter dormancy with strong root development',
 '2024-10-15', 'Dr. Robert Chen', '2024-10-15 14:15:00', '2024-10-15 14:15:00'),

('TRACK004', 'BATCH2024003', 'Flowering Stage R2', 'Field C-05, East Plot',
 '39.1836N, 96.5717W', 2800.00, NULL, 8.90,
 'Excellent flowering uniformity, no visible SCN symptoms in resistant lines',
 '2024-07-10', 'Dr. Lisa Martinez', '2024-07-10 11:45:00', '2024-07-10 11:45:00'),

('TRACK005', 'BATCH2024004', 'Heading and Flowering', 'Field D-03, West Block',
 '44.9778N, 93.2650W', 3500.00, NULL, 9.35,
 'Uniform heading, excellent spike characteristics, no spot blotch observed',
 '2024-06-25', 'Dr. Patricia Anderson', '2024-06-25 13:20:00', '2024-06-25 13:20:00'),

('TRACK006', 'BATCH2024001', 'Grain Fill R5', 'Field A-12, North Block',
 '42.0344N, 93.6204W', 4500.00, 4680.50, 9.10,
 'Excellent kernel development, minimal disease pressure, plants showing good drought tolerance',
 '2024-08-15', 'Dr. Michael Stevens', '2024-08-15 15:30:00', '2024-08-15 15:30:00');


-- =========================================================================================================
-- 4. SEED INFORMATION SERVICE MODULE
-- =========================================================================================================

-- ---------------------------------------------------------------------------------------------------------
-- 4.1 Seed Promotion Information
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_promotion_info` VALUES
('PROMO001', 'ENT001', 'Introducing Golden Harvest Corn GH-2024: The Future of Corn Production',
 '/uploads/videos/promo_gh2024.mp4',
 'Discover the breakthrough performance of Golden Harvest Corn GH-2024. Superior yields, exceptional disease resistance, and outstanding drought tolerance make this the premier choice for Midwest producers.',
 'GH-2024, GH-2023, GH-2022 Elite', '2024-02-21 09:00:00', 180,
 'https://www.greenfields.com/varieties/gh-2024', 2847),

('PROMO002', 'ENT002', 'ProWheat Elite PW-500: Premium Quality Winter Wheat',
 '/uploads/videos/promo_pw500.mp4',
 'ProWheat Elite PW-500 sets new standards in bread wheat quality. Featuring superior protein content and comprehensive disease resistance package for Great Plains production systems.',
 'PW-500, PW-450, PW-400 Series', '2024-02-26 10:30:00', 180,
 'https://www.pioneerbiotech.com/wheat/pw-500', 1956),

('PROMO003', 'ENT004', 'SuperSoy HP-3000: Next Generation Soybean Genetics',
 '/uploads/videos/promo_hp3000.mp4',
 'SuperSoy HP-3000 combines industry-leading SCN resistance with exceptional yield potential and superior seed quality. The smart choice for profitable soybean production.',
 'HP-3000, HP-2800, HP-2500', '2024-03-11 08:45:00', 180,
 'https://www.harvestpro.com/soybean/hp-3000', 3124),

('PROMO004', 'ENT005', 'NorthStar Barley NS-150: Malting Excellence Redefined',
 '/uploads/videos/promo_ns150.mp4',
 'NorthStar Barley NS-150 delivers premium malting quality with exceptional disease resistance. Developed specifically for craft brewing and commercial malting industries.',
 'NS-150, NS-120, NS-100 Premium', '2024-03-16 11:15:00', 180,
 'https://www.globalseeds.com/barley/ns-150', 1538),

('PROMO005', 'ENT001', 'Premium Oat PO-2024: Organic Production Champion',
 '/uploads/videos/promo_po2024.mp4',
 'Premium Oat PO-2024 is specifically developed for organic production systems. High groat percentage and excellent standability make it ideal for sustainable agriculture.',
 'PO-2024, PO-2023 Organic', '2024-03-18 14:00:00', 90,
 'https://www.greenfields.com/oat/po-2024', 892);

-- ---------------------------------------------------------------------------------------------------------
-- 4.2 Seed Variety Query Records
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_variety_query_record` VALUES
('QUERY001', 'high yield corn hybrid', '2024-03-20 09:15:32', '192.168.1.101', 12, 'PUB001'),
('QUERY002', 'drought tolerant corn', '2024-03-20 10:22:18', '192.168.1.102', 8, 'PUB001'),
('QUERY003', 'winter wheat stripe rust resistance', '2024-03-20 11:35:45', '192.168.1.103', 5, 'PUB002'),
('QUERY004', 'high protein wheat variety', '2024-03-20 13:48:22', '192.168.1.104', 7, 'PUB002'),
('QUERY005', 'soybean SCN resistant', '2024-03-20 14:52:10', '192.168.1.105', 15, 'PUB003'),
('QUERY006', 'high protein soybean', '2024-03-20 15:18:37', '192.168.1.106', 9, 'PUB003'),
('QUERY007', 'malting barley variety', '2024-03-20 16:25:54', '192.168.1.107', 4, 'PUB004'),
('QUERY008', 'spring barley high yield', '2024-03-21 08:33:12', '192.168.1.108', 6, 'PUB004'),
('QUERY009', 'organic oat production', '2024-03-21 09:41:28', '192.168.1.109', 3, NULL),
('QUERY010', 'corn northern leaf blight', '2024-03-21 10:55:16', '192.168.1.110', 11, 'PUB001'),
('QUERY011', 'soybean drought tolerance', '2024-03-21 11:22:43', '192.168.1.111', 13, 'PUB003'),
('QUERY012', 'wheat bread making quality', '2024-03-21 13:47:59', '192.168.1.112', 8, 'PUB002'),
('QUERY013', 'barley beta glucan content', '2024-03-21 14:35:21', '192.168.1.113', 5, 'PUB004'),
('QUERY014', 'hybrid corn Midwest', '2024-03-21 15:18:44', '192.168.1.114', 14, 'PUB001'),
('QUERY015', 'disease resistant wheat', '2024-03-21 16:52:37', '192.168.1.115', 10, 'PUB002');


-- =========================================================================================================
-- 5. SEED DATA COLLECTION MODULE
-- =========================================================================================================

-- ---------------------------------------------------------------------------------------------------------
-- 5.1 Trial Base Data
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_trial_base_data` VALUES
('TRIAL2024001', 'Corn', 'Golden Harvest GH-2024', 'RC-MW-001', 'PROG-CORN-001', 'SUBPROG-HYBRID-001',
 'THEME-DROUGHT-TOLERANCE', 'Iowa', 'Midwest Region', 'Story County', 'Ames Township',
 'Corn Belt Prairie', '42.0344N, 93.6204W', '2024-03-15', 'ACT-YIELD-TRIAL-001', 'KPI-YIELD-001',
 'Spring', '0', 'admin', '2024-03-15 08:00:00', 'admin', '2024-03-15 08:00:00',
 'Multi-location hybrid corn yield trial with focus on drought tolerance evaluation'),

('TRIAL2024002', 'Wheat', 'ProWheat Elite PW-500', 'RC-GP-002', 'PROG-WHEAT-001', 'SUBPROG-WINTER-001',
 'THEME-DISEASE-RESISTANCE', 'Nebraska', 'Great Plains Region', 'Lancaster County', 'Lincoln Township',
 'Mixed Grass Prairie', '40.8136N, 96.7026W', '2024-09-15', 'ACT-DISEASE-EVAL-001', 'KPI-RESIST-001',
 'Fall', '0', 'admin', '2024-09-15 09:30:00', 'admin', '2024-09-15 09:30:00',
 'Winter wheat disease resistance evaluation under natural infection pressure'),

('TRIAL2024003', 'Soybean', 'SuperSoy HP-3000', 'RC-MW-003', 'PROG-SOY-001', 'SUBPROG-SCN-001',
 'THEME-SCN-RESISTANCE', 'Illinois', 'Midwest Region', 'Champaign County', 'Urbana Township',
 'Tallgrass Prairie', '40.1106N, 88.2073W', '2024-05-10', 'ACT-SCN-TRIAL-001', 'KPI-SCN-001',
 'Spring', '0', 'admin', '2024-05-10 10:15:00', 'admin', '2024-05-10 10:15:00',
 'Soybean cyst nematode resistance evaluation in high-pressure environment'),

('TRIAL2024004', 'Barley', 'NorthStar Barley NS-150', 'RC-UM-004', 'PROG-BARLEY-001', 'SUBPROG-MALT-001',
 'THEME-MALTING-QUALITY', 'Minnesota', 'Upper Midwest Region', 'Hennepin County', 'Minneapolis Township',
 'Northern Prairie', '44.9778N, 93.2650W', '2024-04-20', 'ACT-QUALITY-EVAL-001', 'KPI-MALT-001',
 'Spring', '0', 'admin', '2024-04-20 11:00:00', 'admin', '2024-04-20 11:00:00',
 'Malting barley quality evaluation including beta-glucan and protein analysis'),

('TRIAL2024005', 'Oat', 'Premium Oat PO-2024', 'RC-MW-005', 'PROG-OAT-001', 'SUBPROG-ORGANIC-001',
 'THEME-ORGANIC-PRODUCTION', 'Wisconsin', 'Upper Midwest Region', 'Dane County', 'Madison Township',
 'Driftless Area', '43.0731N, 89.4012W', '2024-04-25', 'ACT-ORGANIC-TRIAL-001', 'KPI-ORGANIC-001',
 'Spring', '0', 'admin', '2024-04-25 13:45:00', 'admin', '2024-04-25 13:45:00',
 'Organic oat production trial evaluating performance under certified organic management');

-- ---------------------------------------------------------------------------------------------------------
-- 5.2 Farmer and Plot Data
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_farmer_plot_data` VALUES
('FPLOT001', 'Robert Anderson', 'Male', 'Not applicable', 'Story County Crop Growers Cooperative',
 40468.56, 'HH-2024-001', '+1-515-555-1001', '0', 'admin', '2024-03-15 08:30:00',
 'admin', '2024-03-15 08:30:00', '10 acres demonstration plot for corn hybrid trial'),

('FPLOT002', 'Margaret Thompson', 'Female', 'Not applicable', 'Lancaster Agricultural Alliance',
 32374.85, 'HH-2024-002', '+1-402-555-1002', '0', 'admin', '2024-09-15 10:00:00',
 'admin', '2024-09-15 10:00:00', '8 acres winter wheat disease trial location'),

('FPLOT003', 'James Wilson', 'Male', 'Young farmer (25-35)', 'Champaign Soybean Growers Association',
 28328.04, 'HH-2024-003', '+1-217-555-1003', '0', 'admin', '2024-05-10 10:45:00',
 'admin', '2024-05-10 10:45:00', '7 acres SCN infested field for resistance evaluation'),

('FPLOT004', 'Emily Johnson', 'Female', 'Young farmer (25-35)', 'Minnesota Organic Grain Cooperative',
 24281.23, 'HH-2024-004', '+1-612-555-1004', '0', 'admin', '2024-04-20 11:30:00',
 'admin', '2024-04-20 11:30:00', '6 acres certified organic barley production trial'),

('FPLOT005', 'David Martinez', 'Male', 'Not applicable', 'Dane County Sustainable Agriculture Group',
 20234.28, 'HH-2024-005', '+1-608-555-1005', '0', 'admin', '2024-04-25 14:15:00',
 'admin', '2024-04-25 14:15:00', '5 acres certified organic oat trial plot');

-- ---------------------------------------------------------------------------------------------------------
-- 5.3 Farming Record Data
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_farming_record_data` VALUES
('FARM001', 'Conventional tillage', 'NPK 15-15-15', 180.50, 90.25,
 'Atrazine pre-emergence', 'Pivot irrigation', 3, '2024-06-15',
 'Roundup WeatherMAX', 'Certified hybrid seed from GreenFields',
 '0', 'admin', '2024-03-15 09:00:00', 'admin', '2024-06-15 14:30:00',
 'Corn production with standard fertility and weed management program'),

('FARM002', 'Minimum tillage', 'Urea 46-0-0', 110.75, 110.75,
 'Fungicide: Prosaro', 'Dryland', 0, '2024-05-20',
 'Huskie herbicide post-emergence', 'Certified winter wheat seed from Pioneer BioTech',
 '0', 'admin', '2024-09-15 09:30:00', 'admin', '2024-05-20 10:15:00',
 'Winter wheat with disease management and nitrogen top-dress'),

('FARM003', 'No-till', 'Starter fertilizer 10-34-0', 65.30, 40.15,
 'Seed treatment fungicide', 'Rainfed', 0, '2024-07-10',
 'FirstRate herbicide post-emergence', 'Certified soybean seed from HarvestPro',
 '0', 'admin', '2024-05-10 11:00:00', 'admin', '2024-07-10 09:45:00',
 'No-till soybean production with minimal input strategy'),

('FARM004', 'Organic cultivation', 'Organic compost', 2240.00, 0.00,
 'None (organic certified)', 'Supplemental irrigation', 2, '2024-06-01',
 'Mechanical cultivation', 'Certified organic barley seed from Global Seeds',
 '0', 'admin', '2024-04-20 11:45:00', 'admin', '2024-06-01 13:20:00',
 'Certified organic barley production following USDA organic standards'),

('FARM005', 'Organic reduced tillage', 'Organic fertilizer blend', 1680.00, 0.00,
 'None (organic certified)', 'Rainfed', 0, '2024-06-25',
 'Rotary hoe and mechanical weeding', 'Certified organic oat seed from GreenFields',
 '0', 'admin', '2024-04-25 14:30:00', 'admin', '2024-06-25 11:00:00',
 'Organic oat production with mechanical weed control');

-- ---------------------------------------------------------------------------------------------------------
-- 5.4 Agronomic Trait Data
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_agronomic_trait_data` VALUES
('AGRO001', 245.50, 14, 18.30, 7, 35, 68, 72, 98, 125, 2,
 4680.50, 850.50, 165.40,
 '{"Northern_Corn_Leaf_Blight": 2, "Gray_Leaf_Spot": 1, "Common_Rust": 1}',
 '{"Drought_Stress_Score": 3, "Heat_Stress_Score": 2, "Lodging_Resistance": 9}',
 'Minimal European Corn Borer pressure, no significant damage',
 '/uploads/photos/field_agro001_001.jpg,/uploads/photos/field_agro001_002.jpg',
 '0', 'admin', '2024-08-15 16:00:00', 'admin', '2024-08-15 16:00:00',
 'GH-2024 corn hybrid showing excellent agronomic performance'),

('AGRO002', 95.20, 8, 10.50, 10, 25, 180, 195, 220, 265, 1,
 2850.75, 420.30, 48.60,
 '{"Stripe_Rust": 1, "Leaf_Rust": 2, "Fusarium_Head_Blight": 2}',
 '{"Winter_Hardiness_Score": 9, "Heat_Tolerance_Score": 7, "Lodging_Resistance": 9}',
 'Low aphid pressure, no significant insect damage observed',
 '/uploads/photos/field_agro002_001.jpg,/uploads/photos/field_agro002_002.jpg',
 '0', 'admin', '2024-06-20 14:30:00', 'admin', '2024-06-20 14:30:00',
 'PW-500 winter wheat demonstrating superior disease resistance'),

('AGRO003', 88.30, 6, 0.00, 6, 18, 42, 65, 82, 118, 1,
 3240.80, 0.00, 0.00,
 '{"Sudden_Death_Syndrome": 1, "Brown_Stem_Rot": 1, "Frogeye_Leaf_Spot": 2}',
 '{"Drought_Tolerance_Score": 8, "SCN_Resistance_Score": 9, "Lodging_Resistance": 8}',
 'Low soybean aphid and stink bug pressure throughout season',
 '/uploads/photos/field_agro003_001.jpg,/uploads/photos/field_agro003_002.jpg',
 '0', 'admin', '2024-08-25 15:45:00', 'admin', '2024-08-25 15:45:00',
 'HP-3000 soybean showing excellent SCN resistance and minimal disease'),

('AGRO004', 72.40, 12, 8.80, 5, 15, 58, 68, 75, 95, 1,
 2180.40, 680.25, 52.35,
 '{"Spot_Blotch": 1, "Net_Blotch": 2, "Powdery_Mildew": 1}',
 '{"Lodging_Resistance": 9, "Heat_Tolerance_Score": 7, "Drought_Tolerance_Score": 6}',
 'Minimal aphid pressure, no significant barley fly damage',
 '/uploads/photos/field_agro004_001.jpg,/uploads/photos/field_agro004_002.jpg',
 '0', 'admin', '2024-07-15 13:20:00', 'admin', '2024-07-15 13:20:00',
 'NS-150 barley with excellent disease package and standability'),

('AGRO005', 95.60, 10, 0.00, 6, 20, 65, 78, 88, 105, 2,
 2520.60, 0.00, 0.00,
 '{"Crown_Rust": 2, "Septoria_Leaf_Blotch": 1, "Stem_Rust": 1}',
 '{"Lodging_Resistance": 8, "Drought_Tolerance_Score": 7, "Heat_Tolerance_Score": 6}',
 'Low aphid and armyworm pressure under organic management',
 '/uploads/photos/field_agro005_001.jpg,/uploads/photos/field_agro005_002.jpg',
 '0', 'admin', '2024-08-05 14:50:00', 'admin', '2024-08-05 14:50:00',
 'PO-2024 organic oat with good disease resistance and standability');

-- ---------------------------------------------------------------------------------------------------------
-- 5.5 Environment and Soil Data
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_environment_soil_data` VALUES
('ENV001', 6.50, 1.20, 0.18, 18.50, 185.00, 'Soybean', 'Groundwater well',
 'Level', 0.50, 22.50, 18.30, 75.40, 22.50, 68.50, 3.20, 580.50,
 '2024-07-15 14:00:00', '0', 'admin', '2024-07-15 14:00:00', 'admin', '2024-07-15 14:00:00',
 'Mid-season soil and weather data for corn trial location'),

('ENV002', 7.20, 0.85, 0.22, 22.30, 220.50, 'Wheat', 'Surface water irrigation',
 'Gentle slope', 2.00, 18.30, 12.50, 45.20, 15.80, 55.30, 4.50, 520.30,
 '2024-05-15 13:30:00', '0', 'admin', '2024-05-15 13:30:00', 'admin', '2024-05-15 13:30:00',
 'Spring growth stage environmental conditions for winter wheat'),

('ENV003', 6.80, 1.45, 0.15, 15.80, 165.30, 'Corn', 'Natural rainfall',
 'Level', 0.00, 25.80, 20.40, 35.60, 28.50, 52.20, 2.80, 620.80,
 '2024-07-20 15:45:00', '0', 'admin', '2024-07-20 15:45:00', 'admin', '2024-07-20 15:45:00',
 'Peak growth environmental data for soybean SCN trial'),

('ENV004', 6.20, 1.10, 0.20, 20.50, 195.80, 'Barley', 'Supplemental irrigation',
 'Level', 1.00, 24.30, 16.80, 68.50, 18.20, 62.50, 3.50, 595.20,
 '2024-06-25 12:15:00', '0', 'admin', '2024-06-25 12:15:00', 'admin', '2024-06-25 12:15:00',
 'Flowering stage environmental monitoring for malting barley'),

('ENV005', 6.90, 0.95, 0.25, 25.80, 240.30, 'Wheat', 'Natural rainfall',
 'Gentle slope', 1.50, 21.50, 15.20, 52.30, 17.50, 58.70, 3.80, 545.60,
 '2024-07-10 11:30:00', '0', 'admin', '2024-07-10 11:30:00', 'admin', '2024-07-10 11:30:00',
 'Grain fill stage conditions for organic oat production');

-- ---------------------------------------------------------------------------------------------------------
-- 5.6 Variety Evaluation Data
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_variety_evaluation_data` VALUES
('EVAL001', 'PLOT-A12-001', 4046.86, 475.85, 112.30, 15.50,
 '0', 'admin', '2024-09-25 16:00:00', 'admin', '2024-09-25 16:00:00',
 'GH-2024 corn harvest data - exceeds target yield by 12.3%'),

('EVAL002', 'PLOT-B08-001', 3237.49, 220.45, 68.75, 13.20,
 '0', 'admin', '2024-07-15 14:30:00', 'admin', '2024-07-15 14:30:00',
 'PW-500 winter wheat harvest - excellent test weight at 61.2 lbs/bu'),

('EVAL003', 'PLOT-C05-001', 2832.80, 164.85, 58.40, 12.80,
 '0', 'admin', '2024-10-05 15:45:00', 'admin', '2024-10-05 15:45:00',
 'HP-3000 soybean yield evaluation - protein 40.2%, oil 21.3%'),

('EVAL004', 'PLOT-D03-001', 2428.11, 190.95, 78.60, 11.50,
 '0', 'admin', '2024-08-20 13:20:00', 'admin', '2024-08-20 13:20:00',
 'NS-150 barley harvest - malting specifications met, protein 12.8%'),

('EVAL005', 'PLOT-E07-001', 2023.43, 146.55, 72.45, 13.50,
 '0', 'admin', '2024-08-30 14:50:00', 'admin', '2024-08-30 14:50:00',
 'PO-2024 oat harvest - groat percentage 71.8%, beta-glucan 5.3%');

-- ---------------------------------------------------------------------------------------------------------
-- 5.7 Laboratory Test Data
-- ---------------------------------------------------------------------------------------------------------
INSERT INTO `seed_laboratory_test_data` VALUES
('LAB001', 'SAMPLE-2024-001', 'Fresh harvested seed, well-formed kernels',
 96.50, 99.20, 14.20, 9.80, 0.00,
 'No seed-borne diseases detected, excellent vigor',
 'Traceable to GreenFields Research Station Lot #GH2024-A12-001',
 '/uploads/lab_reports/lab_test_001.pdf',
 '0', 'admin', '2024-10-01 10:30:00', 'admin', '2024-10-01 10:30:00',
 'GH-2024 corn seed quality analysis - exceeds industry standards'),

('LAB002', 'SAMPLE-2024-002', 'Clean wheat seed, uniform size',
 94.80, 98.50, 12.50, 14.80, 0.50,
 'Trace Fusarium detected, within acceptable limits',
 'Traceable to Pioneer BioTech Breeding Station Lot #PW500-B08-001',
 '/uploads/lab_reports/lab_test_002.pdf',
 '0', 'admin', '2024-07-20 11:15:00', 'admin', '2024-07-20 11:15:00',
 'PW-500 wheat seed and grain quality - protein target achieved'),

('LAB003', 'SAMPLE-2024-003', 'High quality soybean seed, bright appearance',
 92.30, 99.80, 11.80, 40.20, 0.00,
 'Excellent seed health, no pathogen contamination',
 'Traceable to HarvestPro Innovation Center Lot #HP3000-C05-001',
 '/uploads/lab_reports/lab_test_003.pdf',
 '0', 'admin', '2024-10-10 13:45:00', 'admin', '2024-10-10 13:45:00',
 'HP-3000 soybean seed certification analysis - premium quality'),

('LAB004', 'SAMPLE-2024-004', 'Uniform barley kernels, bright color',
 97.20, 99.50, 10.80, 12.80, 0.00,
 'Clean seed lot, meets malting specifications',
 'Traceable to Global Seeds Research Facility Lot #NS150-D03-001',
 '/uploads/lab_reports/lab_test_004.pdf',
 '0', 'admin', '2024-08-25 14:20:00', 'admin', '2024-08-25 14:20:00',
 'NS-150 barley malting quality analysis - certified malting grade'),

('LAB005', 'SAMPLE-2024-005', 'White oat kernels, excellent appearance',
 95.50, 99.70, 12.20, 16.50, 0.00,
 'Organic certified, no chemical residues detected',
 'Traceable to GreenFields Organic Division Lot #PO2024-E07-001',
 '/uploads/lab_reports/lab_test_005.pdf',
 '0', 'admin', '2024-09-05 15:30:00', 'admin', '2024-09-05 15:30:00',
 'PO-2024 organic oat certification - meets USDA organic standards');


-- =========================================================================================================
-- Data Integrity Verification
-- =========================================================================================================
-- The following queries can be used to verify foreign key relationships and data consistency:
--
-- 1. Verify all variety registrations have valid enterprise references:
--    SELECT COUNT(*) FROM variety_registration vr
--    LEFT JOIN enterprise_info ei ON vr.enterprise_id = ei.enterprise_id
--    WHERE ei.enterprise_id IS NULL;
--    -- Expected result: 0
--
-- 2. Verify all breeding plans have valid enterprise references:
--    SELECT COUNT(*) FROM breeding_plan bp
--    LEFT JOIN enterprise_info ei ON bp.enterprise_id = ei.enterprise_id
--    WHERE ei.enterprise_id IS NULL;
--    -- Expected result: 0
--
-- 3. Verify all promotion records have valid enterprise references:
--    SELECT COUNT(*) FROM seed_promotion_info spi
--    LEFT JOIN enterprise_info ei ON spi.enterprise_id = ei.enterprise_id
--    WHERE ei.enterprise_id IS NULL;
--    -- Expected result: 0
--
-- =========================================================================================================

SET FOREIGN_KEY_CHECKS = 1;

-- =========================================================================================================
-- End of English Test Data for IGDP Seed Management Module
-- =========================================================================================================
-- Total records inserted:
-- - enterprise_info: 7 records
-- - enterprise_audit: 6 records
-- - variety_registration: 6 records
-- - variety_audit: 5 records
-- - variety_publish: 4 records
-- - breeding_plan: 5 records
-- - breeding_material: 6 records
-- - breeding_tracking: 6 records
-- - seed_promotion_info: 5 records
-- - seed_variety_query_record: 15 records
-- - seed_trial_base_data: 5 records
-- - seed_farmer_plot_data: 5 records
-- - seed_farming_record_data: 5 records
-- - seed_agronomic_trait_data: 5 records
-- - seed_environment_soil_data: 5 records
-- - seed_variety_evaluation_data: 5 records
-- - seed_laboratory_test_data: 5 records
-- =========================================================================================================
