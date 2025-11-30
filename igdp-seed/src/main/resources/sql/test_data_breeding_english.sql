-- =========================================================================================================
-- IGDP Seed Module - Breeding Management English Test Data
-- =========================================================================================================
-- Description: Comprehensive test data for breeding management system
-- Tables: breeding_plan, breeding_material, breeding_tracking
-- All data uses English field values and maintains proper foreign key relationships
-- =========================================================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Clear existing data (optional - uncomment if needed)
-- DELETE FROM breeding_tracking;
-- DELETE FROM breeding_material;
-- DELETE FROM breeding_plan;

-- =========================================================================================================
-- 1. BREEDING PLAN (育种计划表)
-- =========================================================================================================
-- Propagation Levels: Breeder Seed, Foundation Seed, Registered Seed, Certified Seed
-- Crop Types: Corn, Wheat, Soybean, Rice, Cotton, Barley, Sorghum, Oat
-- =========================================================================================================

INSERT INTO `breeding_plan` (`plan_id`, `enterprise_id`, `plan_name`, `breeding_year`, `batch_id`,
    `planting_base`, `crop_type`, `variety_name`, `propagation_level`, `parent_seed_source`,
    `person_in_charge`, `start_date`, `end_date`, `breeding_goal`, `remarks`, `create_time`, `update_time`) VALUES

-- ===== ENT001: GreenFields Seed Corporation - Corn & Oat Programs =====
('PLAN001', 'ENT001', 'Elite Corn Hybrid Development Program 2024', 2024, 'BATCH2024001',
 'GreenFields Research Station, Ames, Iowa', 'Corn', 'Golden Harvest GH-2024', 'Breeder Seed',
 'B73 inbred line from USDA-ARS National Plant Germplasm System; Mo17 inbred line from University of Missouri',
 'Dr. Sarah Johnson', '2024-03-01', '2024-10-31',
 'Develop high-yielding F1 hybrid corn with enhanced drought tolerance, Northern Corn Leaf Blight resistance, and improved stalk strength for Midwest production regions. Target yield: 220 bu/acre under optimal conditions.',
 'Phase 1 of 3-year hybrid development program. Collaboration with Iowa State University.',
 '2024-01-15 09:00:00', '2024-03-20 14:30:00'),

('PLAN002', 'ENT001', 'Drought-Tolerant Corn Line Selection 2024', 2024, 'BATCH2024002',
 'GreenFields Arid Research Center, Garden City, Kansas', 'Corn', 'DroughtMaster DM-500', 'Foundation Seed',
 'Proprietary drought-tolerant inbred lines DT-101 and DT-205 from internal breeding program',
 'Dr. Michael Stevens', '2024-03-15', '2024-10-15',
 'Select and advance drought-tolerant corn lines with 15% yield advantage under water-limited conditions. Focus on root architecture and stomatal conductance traits.',
 'Water stress trials with 50% irrigation reduction treatment.',
 '2024-02-01 10:30:00', '2024-04-05 11:20:00'),

('PLAN003', 'ENT001', 'Organic Oat Variety Development 2024', 2024, 'BATCH2024003',
 'GreenFields Organic Research Farm, Story County, Iowa', 'Oat', 'Premium Oat PO-2024', 'Registered Seed',
 'Buff and Caliber varieties from University of Wisconsin Oat Breeding Program',
 'Dr. Emily Richardson', '2024-04-01', '2024-09-30',
 'Develop white oat variety optimized for organic production with high groat percentage (>72%), crown rust resistance, excellent standability, and beta-glucan content >5%.',
 'USDA Organic certified trial. No synthetic inputs applied.',
 '2024-02-20 13:45:00', '2024-04-15 09:10:00'),

-- ===== ENT002: Pioneer BioTech Seeds Ltd - Wheat Programs =====
('PLAN004', 'ENT002', 'Winter Wheat Elite Germplasm Enhancement 2024', 2024, 'BATCH2024004',
 'Pioneer BioTech Breeding Station, Lincoln, Nebraska', 'Wheat', 'ProWheat Elite PW-500', 'Breeder Seed',
 'Jagger variety from Kansas State University; 2174 from Oklahoma State University; Overley from K-State',
 'Dr. Jennifer Wang', '2024-09-01', '2025-07-31',
 'Develop high-protein (>14%) hard red winter wheat with comprehensive disease resistance including Stripe Rust, Leaf Rust, and Fusarium Head Blight. Target test weight: 62 lb/bu.',
 'Multi-environment trial across 5 Great Plains locations.',
 '2024-01-20 08:00:00', '2024-09-15 10:45:00'),

('PLAN005', 'ENT002', 'Heat-Tolerant Spring Wheat Breeding 2024', 2024, 'BATCH2024005',
 'Pioneer BioTech Southern Station, Stillwater, Oklahoma', 'Wheat', 'HeatShield HS-300', 'Foundation Seed',
 'TAM 111 from Texas A&M AgriLife; Gallagher from Oklahoma State University breeding program',
 'Dr. Robert Chen', '2024-02-15', '2024-07-15',
 'Breed spring wheat varieties with enhanced heat tolerance during grain fill, targeting 10% yield stability improvement under heat stress (>35C during flowering).',
 'Heat stress simulation using late planting dates.',
 '2024-01-25 11:00:00', '2024-03-01 14:20:00'),

-- ===== ENT004: HarvestPro Seed Industries - Soybean & Cotton Programs =====
('PLAN006', 'ENT004', 'SCN-Resistant Soybean Development 2024', 2024, 'BATCH2024006',
 'HarvestPro Innovation Center, Manhattan, Kansas', 'Soybean', 'SuperSoy HP-3000', 'Breeder Seed',
 'Williams 82 background with PI 88788 SCN resistance; Peking-type resistance from PI 548402',
 'Dr. Emma Davis', '2024-04-15', '2024-10-31',
 'Develop soybean varieties with pyramided SCN resistance to races 1, 2, 3, 5, and 14. Target: >65 bu/acre yield with combined protein+oil >60%.',
 'High SCN pressure field with documented race mixture.',
 '2024-02-05 09:30:00', '2024-04-20 15:40:00'),

('PLAN007', 'ENT004', 'High-Oil Soybean Line Advancement 2024', 2024, 'BATCH2024007',
 'HarvestPro Genetics Lab, Columbia, Missouri', 'Soybean', 'OilMax OM-2500', 'Registered Seed',
 'High-oil breeding lines from University of Missouri Soybean Breeding; N7103 from Northrup King',
 'Dr. Lisa Martinez', '2024-04-20', '2024-10-15',
 'Advance high-oil soybean lines targeting oil content >23% while maintaining protein >38%. Focus on industrial oil market requirements.',
 'Near-infrared spectroscopy for rapid oil content screening.',
 '2024-03-01 10:15:00', '2024-04-25 11:30:00'),

('PLAN008', 'ENT004', 'Premium Cotton Fiber Quality Program 2024', 2024, 'BATCH2024008',
 'HarvestPro Cotton Research Station, Lubbock, Texas', 'Cotton', 'FiberPro FP-800', 'Foundation Seed',
 'Deltapine 1646 B2XF from Bayer CropScience; PHY 400 W3FE from Corteva Agriscience',
 'Dr. James Thompson', '2024-04-01', '2024-11-30',
 'Develop upland cotton variety with superior fiber quality: staple length >1.20 inches, micronaire 3.8-4.2, strength >32 g/tex. Target lint yield: 1,400 lb/acre.',
 'Irrigated trial with deficit irrigation treatment for fiber quality.',
 '2024-02-15 14:00:00', '2024-04-10 09:25:00'),

-- ===== ENT005: Global Seeds International - Barley, Sorghum & Rice Programs =====
('PLAN009', 'ENT005', 'Malting Barley Quality Enhancement 2024', 2024, 'BATCH2024009',
 'Global Seeds Research Facility, Minneapolis, Minnesota', 'Barley', 'NorthStar Barley NS-150', 'Breeder Seed',
 'Conlon variety from North Dakota State University; Drummond from University of Minnesota Barley Breeding',
 'Dr. Michelle Taylor', '2024-04-01', '2024-09-15',
 'Develop two-row spring barley with premium malting quality: protein 11.5-13.5%, beta-glucan >5%, plump kernels >90%, germination >95%. Meet craft brewing industry specifications.',
 'Collaboration with American Malting Barley Association.',
 '2024-02-10 11:45:00', '2024-04-05 13:50:00'),

('PLAN010', 'ENT005', 'Feed Barley Yield Improvement 2024', 2024, 'BATCH2024010',
 'Global Seeds Northern Station, Fargo, North Dakota', 'Barley', 'FeedMax FM-200', 'Certified Seed',
 'Stellar-ND from NDSU; Pinnacle from Montana State University barley breeding program',
 'Dr. Patricia Anderson', '2024-04-10', '2024-09-20',
 'Increase feed barley yield potential by 8% over current check varieties while maintaining acceptable grain quality for livestock feed applications.',
 'Large-scale strip trials on commercial farms.',
 '2024-02-25 08:30:00', '2024-04-15 10:15:00'),

('PLAN011', 'ENT005', 'Grain Sorghum Hybrid Development 2024', 2024, 'BATCH2024011',
 'Global Seeds Southern Research Center, College Station, Texas', 'Sorghum', 'PowerGrain PG-700', 'Breeder Seed',
 'ATx631 and BTx631 from Texas A&M AgriLife; RTx430 restorer line from University of Nebraska',
 'Dr. David Wilson', '2024-03-15', '2024-10-31',
 'Develop grain sorghum hybrid with enhanced yield (>120 bu/acre), charcoal rot resistance, and stay-green trait for terminal drought tolerance.',
 'Hybrid testcross evaluation across 8 Texas Panhandle locations.',
 '2024-01-30 10:00:00', '2024-03-20 14:40:00'),

('PLAN012', 'ENT005', 'Medium-Grain Rice Quality Breeding 2024', 2024, 'BATCH2024012',
 'Global Seeds Delta Research Farm, Stuttgart, Arkansas', 'Rice', 'PearlRice PR-1000', 'Foundation Seed',
 'Cheniere variety from Louisiana State University; Jupiter from University of Arkansas Rice Breeding',
 'Dr. Kevin Park', '2024-04-01', '2024-10-15',
 'Breed medium-grain rice with excellent milling quality (head rice >65%), low chalk (<5%), and blast resistance. Target yield: 8,500 lb/acre rough rice.',
 'Flooded paddy culture with standard water management.',
 '2024-02-08 09:15:00', '2024-04-05 11:30:00');


-- =========================================================================================================
-- 2. BREEDING MATERIAL (育种材料登记表)
-- =========================================================================================================
-- Seed Types: Inbred Line, F1 Hybrid Seed, Backcross Material, Doubled Haploid,
--             Recombinant Inbred Line, Near-Isogenic Line, Landrace, Elite Germplasm
-- =========================================================================================================

INSERT INTO `breeding_material` (`material_id`, `registration_code`, `batch_id`, `warehouse_in_id`,
    `seed_type`, `quantity`, `source_entity`, `receive_date`, `lab_test_report_url`,
    `operator`, `operation_org`, `operation_time`) VALUES

-- ===== Materials for BATCH2024001 (Corn Hybrid Development) =====
('MAT001', 'BM-2024-001-001', 'BATCH2024001', 'WH-2024-0001',
 'Inbred Line', 150.00, 'USDA-ARS North Central Regional Plant Introduction Station, Ames, Iowa',
 '2024-02-15', '/uploads/lab_reports/mat001_germination_purity.pdf',
 'Sarah Johnson', 'GreenFields Research Division', '2024-02-15 09:30:00'),

('MAT002', 'BM-2024-001-002', 'BATCH2024001', 'WH-2024-0002',
 'Inbred Line', 145.50, 'University of Missouri Plant Sciences Division, Columbia, Missouri',
 '2024-02-15', '/uploads/lab_reports/mat002_germination_purity.pdf',
 'Sarah Johnson', 'GreenFields Research Division', '2024-02-15 10:15:00'),

('MAT003', 'BM-2024-001-003', 'BATCH2024001', 'WH-2024-0003',
 'F1 Hybrid Seed', 85.25, 'GreenFields Internal Crossing Block 2023 Season',
 '2024-02-20', '/uploads/lab_reports/mat003_hybrid_verification.pdf',
 'Michael Stevens', 'GreenFields Hybrid Production', '2024-02-20 14:00:00'),

-- ===== Materials for BATCH2024002 (Drought-Tolerant Corn) =====
('MAT004', 'BM-2024-002-001', 'BATCH2024002', 'WH-2024-0004',
 'Elite Germplasm', 120.00, 'GreenFields Drought Tolerance Breeding Program',
 '2024-03-01', '/uploads/lab_reports/mat004_drought_screening.pdf',
 'Michael Stevens', 'GreenFields Arid Research Center', '2024-03-01 08:45:00'),

('MAT005', 'BM-2024-002-002', 'BATCH2024002', 'WH-2024-0005',
 'Backcross Material', 95.75, 'International Maize and Wheat Improvement Center (CIMMYT)',
 '2024-03-05', '/uploads/lab_reports/mat005_marker_verification.pdf',
 'Michael Stevens', 'GreenFields Arid Research Center', '2024-03-05 11:20:00'),

-- ===== Materials for BATCH2024003 (Organic Oat) =====
('MAT006', 'BM-2024-003-001', 'BATCH2024003', 'WH-2024-0006',
 'Certified Seed', 200.00, 'University of Wisconsin Oat Breeding Program, Madison',
 '2024-03-15', '/uploads/lab_reports/mat006_organic_certification.pdf',
 'Emily Richardson', 'GreenFields Organic Division', '2024-03-15 09:00:00'),

('MAT007', 'BM-2024-003-002', 'BATCH2024003', 'WH-2024-0007',
 'Recombinant Inbred Line', 75.50, 'USDA-ARS Small Grains Research Facility, Aberdeen, Idaho',
 '2024-03-18', '/uploads/lab_reports/mat007_ril_pedigree.pdf',
 'Emily Richardson', 'GreenFields Organic Division', '2024-03-18 10:30:00'),

-- ===== Materials for BATCH2024004 (Winter Wheat Elite) =====
('MAT008', 'BM-2024-004-001', 'BATCH2024004', 'WH-2024-0008',
 'Foundation Seed', 280.00, 'Kansas State University Wheat Genetics Resource Center',
 '2024-08-01', '/uploads/lab_reports/mat008_disease_screening.pdf',
 'Jennifer Wang', 'Pioneer BioTech Breeding Department', '2024-08-01 08:30:00'),

('MAT009', 'BM-2024-004-002', 'BATCH2024004', 'WH-2024-0009',
 'Doubled Haploid', 65.25, 'Heartland Plant Innovations, Manhattan, Kansas',
 '2024-08-05', '/uploads/lab_reports/mat009_dh_verification.pdf',
 'Jennifer Wang', 'Pioneer BioTech Breeding Department', '2024-08-05 14:15:00'),

('MAT010', 'BM-2024-004-003', 'BATCH2024004', 'WH-2024-0010',
 'Near-Isogenic Line', 42.80, 'Oklahoma State University Wheat Improvement Team',
 '2024-08-10', '/uploads/lab_reports/mat010_nil_characterization.pdf',
 'Robert Chen', 'Pioneer BioTech Genetics Lab', '2024-08-10 11:00:00'),

-- ===== Materials for BATCH2024005 (Heat-Tolerant Wheat) =====
('MAT011', 'BM-2024-005-001', 'BATCH2024005', 'WH-2024-0011',
 'Breeder Seed', 180.00, 'Texas A&M AgriLife Research, College Station',
 '2024-02-01', '/uploads/lab_reports/mat011_heat_tolerance.pdf',
 'Robert Chen', 'Pioneer BioTech Southern Station', '2024-02-01 09:45:00'),

('MAT012', 'BM-2024-005-002', 'BATCH2024005', 'WH-2024-0012',
 'Elite Germplasm', 155.50, 'Oklahoma State University Wheat Breeding Program',
 '2024-02-05', '/uploads/lab_reports/mat012_quality_analysis.pdf',
 'Robert Chen', 'Pioneer BioTech Southern Station', '2024-02-05 13:30:00'),

-- ===== Materials for BATCH2024006 (SCN-Resistant Soybean) =====
('MAT013', 'BM-2024-006-001', 'BATCH2024006', 'WH-2024-0013',
 'Backcross Material', 220.00, 'USDA-ARS Soybean Germplasm Collection, Urbana, Illinois',
 '2024-03-20', '/uploads/lab_reports/mat013_scn_screening.pdf',
 'Emma Davis', 'HarvestPro Genetics Lab', '2024-03-20 10:00:00'),

('MAT014', 'BM-2024-006-002', 'BATCH2024006', 'WH-2024-0014',
 'Recombinant Inbred Line', 165.75, 'University of Illinois Soybean Breeding and Genetics',
 '2024-03-25', '/uploads/lab_reports/mat014_ril_mapping.pdf',
 'Emma Davis', 'HarvestPro Genetics Lab', '2024-03-25 14:20:00'),

('MAT015', 'BM-2024-006-003', 'BATCH2024006', 'WH-2024-0015',
 'Near-Isogenic Line', 88.50, 'University of Missouri Delta Research Center',
 '2024-03-28', '/uploads/lab_reports/mat015_nil_verification.pdf',
 'Lisa Martinez', 'HarvestPro SCN Research Unit', '2024-03-28 09:15:00'),

-- ===== Materials for BATCH2024007 (High-Oil Soybean) =====
('MAT016', 'BM-2024-007-001', 'BATCH2024007', 'WH-2024-0016',
 'Elite Germplasm', 195.00, 'University of Missouri Soybean Breeding Program',
 '2024-04-01', '/uploads/lab_reports/mat016_oil_content.pdf',
 'Lisa Martinez', 'HarvestPro Quality Lab', '2024-04-01 08:30:00'),

('MAT017', 'BM-2024-007-002', 'BATCH2024007', 'WH-2024-0017',
 'Landrace', 125.25, 'USDA Soybean Germplasm Collection - Chinese Accessions',
 '2024-04-05', '/uploads/lab_reports/mat017_composition_analysis.pdf',
 'Lisa Martinez', 'HarvestPro Quality Lab', '2024-04-05 11:45:00'),

-- ===== Materials for BATCH2024008 (Premium Cotton) =====
('MAT018', 'BM-2024-008-001', 'BATCH2024008', 'WH-2024-0018',
 'Foundation Seed', 85.00, 'Bayer CropScience Cotton Breeding, Lubbock, Texas',
 '2024-03-15', '/uploads/lab_reports/mat018_fiber_quality.pdf',
 'James Thompson', 'HarvestPro Cotton Division', '2024-03-15 10:00:00'),

('MAT019', 'BM-2024-008-002', 'BATCH2024008', 'WH-2024-0019',
 'Elite Germplasm', 72.50, 'Corteva Agriscience Cotton Research, Plainview, Texas',
 '2024-03-18', '/uploads/lab_reports/mat019_hvi_testing.pdf',
 'James Thompson', 'HarvestPro Cotton Division', '2024-03-18 14:30:00'),

('MAT020', 'BM-2024-008-003', 'BATCH2024008', 'WH-2024-0020',
 'Inbred Line', 55.75, 'Texas A&M AgriLife Cotton Improvement Program',
 '2024-03-20', '/uploads/lab_reports/mat020_gin_turnout.pdf',
 'James Thompson', 'HarvestPro Cotton Division', '2024-03-20 09:45:00'),

-- ===== Materials for BATCH2024009 (Malting Barley) =====
('MAT021', 'BM-2024-009-001', 'BATCH2024009', 'WH-2024-0021',
 'Foundation Seed', 240.00, 'North Dakota State University Barley Breeding Program',
 '2024-03-10', '/uploads/lab_reports/mat021_malt_quality.pdf',
 'Michelle Taylor', 'Global Seeds Breeding Program', '2024-03-10 08:00:00'),

('MAT022', 'BM-2024-009-002', 'BATCH2024009', 'WH-2024-0022',
 'Doubled Haploid', 95.50, 'University of Minnesota Barley Improvement Program',
 '2024-03-12', '/uploads/lab_reports/mat022_dh_uniformity.pdf',
 'Michelle Taylor', 'Global Seeds Breeding Program', '2024-03-12 11:15:00'),

-- ===== Materials for BATCH2024010 (Feed Barley) =====
('MAT023', 'BM-2024-010-001', 'BATCH2024010', 'WH-2024-0023',
 'Certified Seed', 320.00, 'Montana State University Barley Breeding, Bozeman',
 '2024-03-25', '/uploads/lab_reports/mat023_feed_quality.pdf',
 'Patricia Anderson', 'Global Seeds Northern Operations', '2024-03-25 09:30:00'),

('MAT024', 'BM-2024-010-002', 'BATCH2024010', 'WH-2024-0024',
 'Elite Germplasm', 185.25, 'NDSU Foundation Seed Stocks, Fargo',
 '2024-03-28', '/uploads/lab_reports/mat024_yield_data.pdf',
 'Patricia Anderson', 'Global Seeds Northern Operations', '2024-03-28 13:45:00'),

-- ===== Materials for BATCH2024011 (Grain Sorghum) =====
('MAT025', 'BM-2024-011-001', 'BATCH2024011', 'WH-2024-0025',
 'Inbred Line', 135.00, 'Texas A&M AgriLife Sorghum Improvement Program',
 '2024-02-20', '/uploads/lab_reports/mat025_a_line_test.pdf',
 'David Wilson', 'Global Seeds Sorghum Division', '2024-02-20 10:30:00'),

('MAT026', 'BM-2024-011-002', 'BATCH2024011', 'WH-2024-0026',
 'Inbred Line', 128.75, 'University of Nebraska Sorghum Breeding, Lincoln',
 '2024-02-22', '/uploads/lab_reports/mat026_r_line_test.pdf',
 'David Wilson', 'Global Seeds Sorghum Division', '2024-02-22 14:00:00'),

('MAT027', 'BM-2024-011-003', 'BATCH2024011', 'WH-2024-0027',
 'F1 Hybrid Seed', 210.50, 'Global Seeds Sorghum Hybrid Production Block 2023',
 '2024-03-01', '/uploads/lab_reports/mat027_hybrid_purity.pdf',
 'David Wilson', 'Global Seeds Sorghum Division', '2024-03-01 09:00:00'),

-- ===== Materials for BATCH2024012 (Medium-Grain Rice) =====
('MAT028', 'BM-2024-012-001', 'BATCH2024012', 'WH-2024-0028',
 'Foundation Seed', 450.00, 'Louisiana State University Rice Research Station, Crowley',
 '2024-03-05', '/uploads/lab_reports/mat028_milling_quality.pdf',
 'Kevin Park', 'Global Seeds Rice Division', '2024-03-05 08:15:00'),

('MAT029', 'BM-2024-012-002', 'BATCH2024012', 'WH-2024-0029',
 'Breeder Seed', 185.25, 'University of Arkansas Rice Breeding Program, Stuttgart',
 '2024-03-08', '/uploads/lab_reports/mat029_grain_quality.pdf',
 'Kevin Park', 'Global Seeds Rice Division', '2024-03-08 10:45:00'),

('MAT030', 'BM-2024-012-003', 'BATCH2024012', 'WH-2024-0030',
 'Recombinant Inbred Line', 125.00, 'USDA-ARS Dale Bumpers National Rice Research Center',
 '2024-03-10', '/uploads/lab_reports/mat030_blast_screening.pdf',
 'Kevin Park', 'Global Seeds Rice Division', '2024-03-10 13:30:00');


-- =========================================================================================================
-- 3. BREEDING TRACKING (育种跟踪记录表)
-- =========================================================================================================
-- Stage Names for different crops:
-- Corn: Planting, Emergence (VE), Vegetative Growth (V3-V12), Tasseling (VT), Silking (R1),
--       Blister (R2), Milk (R3), Dough (R4), Dent (R5), Physiological Maturity (R6), Harvest
-- Wheat: Planting, Germination, Tillering, Stem Extension, Booting, Heading, Flowering,
--        Milk Development, Dough Development, Ripening, Harvest
-- Soybean: Planting, Emergence (VE), Vegetative (V1-Vn), Flowering (R1-R2), Pod Development (R3-R4),
--          Seed Development (R5-R6), Maturation (R7-R8), Harvest
-- =========================================================================================================

INSERT INTO `breeding_tracking` (`tracking_id`, `batch_id`, `stage_name`, `location`, `coordinates`,
    `expected_yield`, `actual_yield`, `field_inspection_score`, `disease_observation`,
    `stage_completion_date`, `recorder`, `record_time`, `update_time`) VALUES

-- ===== Tracking for BATCH2024001 (Corn Hybrid Development - Complete Season) =====
('TRACK001', 'BATCH2024001', 'Planting', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, NULL, NULL,
 'Pre-plant soil conditions optimal. Seed treatment applied: fungicide + insecticide.',
 '2024-04-25', 'Dr. Michael Stevens', '2024-04-25 16:00:00', '2024-04-25 16:00:00'),

('TRACK002', 'BATCH2024001', 'Emergence (VE)', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, NULL, 9.50,
 'Excellent emergence uniformity at 96%. No early-season disease pressure observed.',
 '2024-05-05', 'Dr. Michael Stevens', '2024-05-05 10:30:00', '2024-05-05 10:30:00'),

('TRACK003', 'BATCH2024001', 'Vegetative Growth V6', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, NULL, 9.25,
 'Plants showing excellent vigor and uniformity. Root development ahead of schedule. No foliar diseases detected.',
 '2024-05-28', 'Dr. Sarah Johnson', '2024-05-28 14:15:00', '2024-05-28 14:15:00'),

('TRACK004', 'BATCH2024001', 'Vegetative Growth V12', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, NULL, 9.00,
 'Canopy closure complete. Minor Gray Leaf Spot lesions observed on lower leaves (severity 1/9). Good drought stress response.',
 '2024-06-18', 'Dr. Michael Stevens', '2024-06-18 09:45:00', '2024-06-18 09:45:00'),

('TRACK005', 'BATCH2024001', 'Tasseling (VT)', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, NULL, 9.35,
 'Excellent tassel emergence and pollen shed. Anthesis-silking interval <3 days. No significant insect damage.',
 '2024-07-08', 'Dr. Sarah Johnson', '2024-07-08 11:20:00', '2024-07-08 11:20:00'),

('TRACK006', 'BATCH2024001', 'Silking (R1)', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, NULL, 9.40,
 'Uniform silk emergence. Excellent pollination observed. Ear shoot development uniform across plots.',
 '2024-07-12', 'Dr. Michael Stevens', '2024-07-12 10:00:00', '2024-07-12 10:00:00'),

('TRACK007', 'BATCH2024001', 'Grain Fill (R4-R5)', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, NULL, 9.20,
 'Kernel development progressing well. Dent formation beginning. Northern Corn Leaf Blight rating: 2/9 (resistant response).',
 '2024-08-15', 'Dr. Sarah Johnson', '2024-08-15 15:30:00', '2024-08-15 15:30:00'),

('TRACK008', 'BATCH2024001', 'Physiological Maturity (R6)', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, NULL, 9.10,
 'Black layer formation confirmed. Excellent stalk quality maintained. No significant stalk rot observed.',
 '2024-09-18', 'Dr. Michael Stevens', '2024-09-18 11:45:00', '2024-09-18 11:45:00'),

('TRACK009', 'BATCH2024001', 'Harvest', 'Field A-12, North Block, GreenFields Research Station',
 '42.0344N, 93.6204W', 11000.00, 11847.50, 9.30,
 'Harvest completed at 15.2% moisture. Yield exceeded target by 7.7%. Excellent test weight at 58.5 lb/bu.',
 '2024-10-05', 'Dr. Sarah Johnson', '2024-10-05 16:30:00', '2024-10-05 16:30:00'),

-- ===== Tracking for BATCH2024002 (Drought-Tolerant Corn - Water Stress Trial) =====
('TRACK010', 'BATCH2024002', 'Planting', 'Field D-05, Stress Block, GreenFields Arid Research Center',
 '37.9716N, 100.8726W', 8500.00, NULL, NULL,
 'Planted under reduced irrigation protocol. Soil moisture at 65% field capacity.',
 '2024-04-28', 'Dr. Michael Stevens', '2024-04-28 09:00:00', '2024-04-28 09:00:00'),

('TRACK011', 'BATCH2024002', 'Vegetative Stress Period V8-V12', 'Field D-05, Stress Block, GreenFields Arid Research Center',
 '37.9716N, 100.8726W', 8500.00, NULL, 8.75,
 'Water stress imposed at V8. Drought-tolerant lines showing 15% less leaf rolling than susceptible checks. Root pulling resistance excellent.',
 '2024-06-20', 'Dr. Michael Stevens', '2024-06-20 14:30:00', '2024-06-20 14:30:00'),

('TRACK012', 'BATCH2024002', 'Flowering Under Stress', 'Field D-05, Stress Block, GreenFields Arid Research Center',
 '37.9716N, 100.8726W', 8500.00, NULL, 8.50,
 'ASI maintained <5 days under stress. Ear tip blanking reduced by 25% in tolerant lines vs checks.',
 '2024-07-15', 'Dr. Michael Stevens', '2024-07-15 10:15:00', '2024-07-15 10:15:00'),

('TRACK013', 'BATCH2024002', 'Harvest - Stress Treatment', 'Field D-05, Stress Block, GreenFields Arid Research Center',
 '37.9716N, 100.8726W', 8500.00, 9180.25, 8.85,
 'Drought-tolerant lines yielded 8% above target under 50% irrigation. Excellent kernel weight maintenance.',
 '2024-10-08', 'Dr. Michael Stevens', '2024-10-08 15:00:00', '2024-10-08 15:00:00'),

-- ===== Tracking for BATCH2024003 (Organic Oat) =====
('TRACK014', 'BATCH2024003', 'Planting', 'Field O-03, Certified Organic Block, GreenFields Organic Farm',
 '42.0508N, 93.6152W', 4200.00, NULL, NULL,
 'Organic certified seed planted. No seed treatment applied per organic protocol.',
 '2024-04-15', 'Dr. Emily Richardson', '2024-04-15 08:30:00', '2024-04-15 08:30:00'),

('TRACK015', 'BATCH2024003', 'Tillering', 'Field O-03, Certified Organic Block, GreenFields Organic Farm',
 '42.0508N, 93.6152W', 4200.00, NULL, 9.00,
 'Excellent tiller development averaging 4.5 tillers/plant. Mechanical cultivation completed for weed control.',
 '2024-05-20', 'Dr. Emily Richardson', '2024-05-20 11:00:00', '2024-05-20 11:00:00'),

('TRACK016', 'BATCH2024003', 'Heading and Flowering', 'Field O-03, Certified Organic Block, GreenFields Organic Farm',
 '42.0508N, 93.6152W', 4200.00, NULL, 8.85,
 'Uniform heading observed. Crown rust pressure moderate (rating 3/9). Resistant lines showing <5% infection.',
 '2024-06-25', 'Dr. Emily Richardson', '2024-06-25 14:45:00', '2024-06-25 14:45:00'),

('TRACK017', 'BATCH2024003', 'Grain Fill and Maturity', 'Field O-03, Certified Organic Block, GreenFields Organic Farm',
 '42.0508N, 93.6152W', 4200.00, NULL, 9.10,
 'Excellent grain fill. Lodging minimal at 5%. Test weight averaging 38 lb/bu.',
 '2024-07-25', 'Dr. Emily Richardson', '2024-07-25 10:30:00', '2024-07-25 10:30:00'),

('TRACK018', 'BATCH2024003', 'Harvest', 'Field O-03, Certified Organic Block, GreenFields Organic Farm',
 '42.0508N, 93.6152W', 4200.00, 4485.75, 9.15,
 'Harvest at 12.5% moisture. Groat percentage 72.8%. Beta-glucan content 5.4%. Meets organic premium market specifications.',
 '2024-08-10', 'Dr. Emily Richardson', '2024-08-10 16:00:00', '2024-08-10 16:00:00'),

-- ===== Tracking for BATCH2024004 (Winter Wheat - Fall Planting through Spring) =====
('TRACK019', 'BATCH2024004', 'Fall Planting', 'Field W-08, South Section, Pioneer BioTech Station',
 '40.8136N, 96.7026W', 4500.00, NULL, NULL,
 'Optimal planting conditions. Soil temperature 55F. Seed treatment: fungicide + insecticide.',
 '2024-09-28', 'Dr. Jennifer Wang', '2024-09-28 14:00:00', '2024-09-28 14:00:00'),

('TRACK020', 'BATCH2024004', 'Fall Establishment and Tillering', 'Field W-08, South Section, Pioneer BioTech Station',
 '40.8136N, 96.7026W', 4500.00, NULL, 9.25,
 'Excellent fall stand. Average 3.2 tillers/plant before dormancy. Good crown development for winter survival.',
 '2024-11-10', 'Dr. Robert Chen', '2024-11-10 10:30:00', '2024-11-10 10:30:00'),

('TRACK021', 'BATCH2024004', 'Spring Green-up', 'Field W-08, South Section, Pioneer BioTech Station',
 '40.8136N, 96.7026W', 4500.00, NULL, 9.40,
 'Excellent winter survival at 98%. Vigorous spring growth. Nitrogen top-dress applied at 60 lb/acre.',
 '2024-03-15', 'Dr. Jennifer Wang', '2024-03-15 09:15:00', '2024-03-15 09:15:00'),

('TRACK022', 'BATCH2024004', 'Jointing and Stem Extension', 'Field W-08, South Section, Pioneer BioTech Station',
 '40.8136N, 96.7026W', 4500.00, NULL, 9.30,
 'Rapid stem elongation. Plant height uniform. No stripe rust detected despite regional pressure.',
 '2024-04-20', 'Dr. Robert Chen', '2024-04-20 11:45:00', '2024-04-20 11:45:00'),

('TRACK023', 'BATCH2024004', 'Heading and Flowering', 'Field W-08, South Section, Pioneer BioTech Station',
 '40.8136N, 96.7026W', 4500.00, NULL, 9.20,
 'Uniform heading. Excellent floret fertility. Fungicide applied for Fusarium Head Blight prevention.',
 '2024-05-15', 'Dr. Jennifer Wang', '2024-05-15 14:30:00', '2024-05-15 14:30:00'),

('TRACK024', 'BATCH2024004', 'Grain Fill', 'Field W-08, South Section, Pioneer BioTech Station',
 '40.8136N, 96.7026W', 4500.00, NULL, 9.15,
 'Excellent grain fill. No FHB symptoms observed. Test weight developing well at 61.5 lb/bu estimate.',
 '2024-06-10', 'Dr. Robert Chen', '2024-06-10 10:00:00', '2024-06-10 10:00:00'),

('TRACK025', 'BATCH2024004', 'Harvest', 'Field W-08, South Section, Pioneer BioTech Station',
 '40.8136N, 96.7026W', 4500.00, 4725.50, 9.35,
 'Harvest at 12.8% moisture. Test weight 62.3 lb/bu. Protein 14.8%. Exceeds premium wheat specifications.',
 '2024-07-05', 'Dr. Jennifer Wang', '2024-07-05 17:00:00', '2024-07-05 17:00:00'),

-- ===== Tracking for BATCH2024006 (SCN-Resistant Soybean) =====
('TRACK026', 'BATCH2024006', 'Planting', 'Field S-05, High SCN Block, HarvestPro Innovation Center',
 '39.1836N, 96.5717W', 3900.00, NULL, NULL,
 'Planted in field with documented SCN race mixture (races 1, 3, 5, 14). Seed treatment: fungicide + nematicide.',
 '2024-05-10', 'Dr. Emma Davis', '2024-05-10 09:30:00', '2024-05-10 09:30:00'),

('TRACK027', 'BATCH2024006', 'Emergence and Early Vegetative (VE-V3)', 'Field S-05, High SCN Block, HarvestPro Innovation Center',
 '39.1836N, 96.5717W', 3900.00, NULL, 9.45,
 'Excellent emergence at 94%. Resistant lines showing no early SCN symptoms. Susceptible checks showing chlorosis.',
 '2024-05-28', 'Dr. Lisa Martinez', '2024-05-28 11:15:00', '2024-05-28 11:15:00'),

('TRACK028', 'BATCH2024006', 'Flowering (R1-R2)', 'Field S-05, High SCN Block, HarvestPro Innovation Center',
 '39.1836N, 96.5717W', 3900.00, NULL, 9.30,
 'Full bloom reached. SCN egg counts confirm resistant lines suppressing nematode reproduction by >90%.',
 '2024-07-05', 'Dr. Emma Davis', '2024-07-05 10:45:00', '2024-07-05 10:45:00'),

('TRACK029', 'BATCH2024006', 'Pod Development (R3-R4)', 'Field S-05, High SCN Block, HarvestPro Innovation Center',
 '39.1836N, 96.5717W', 3900.00, NULL, 9.25,
 'Excellent pod set averaging 42 pods/plant. No SDS or brown stem rot symptoms. Resistant lines maintaining vigor.',
 '2024-08-02', 'Dr. Lisa Martinez', '2024-08-02 14:20:00', '2024-08-02 14:20:00'),

('TRACK030', 'BATCH2024006', 'Seed Fill (R5-R6)', 'Field S-05, High SCN Block, HarvestPro Innovation Center',
 '39.1836N, 96.5717W', 3900.00, NULL, 9.35,
 'Rapid seed fill. Seed size developing well. No late-season foliar diseases. Canopy retention excellent.',
 '2024-08-28', 'Dr. Emma Davis', '2024-08-28 11:00:00', '2024-08-28 11:00:00'),

('TRACK031', 'BATCH2024006', 'Maturation and Harvest (R7-R8)', 'Field S-05, High SCN Block, HarvestPro Innovation Center',
 '39.1836N, 96.5717W', 3900.00, 4158.75, 9.40,
 'Harvest at 13.1% moisture. Yield 6.7% above target. Protein 40.3%, Oil 21.1%. SCN Female Index <10% on resistant lines.',
 '2024-10-12', 'Dr. Lisa Martinez', '2024-10-12 16:45:00', '2024-10-12 16:45:00'),

-- ===== Tracking for BATCH2024008 (Premium Cotton) =====
('TRACK032', 'BATCH2024008', 'Planting', 'Field C-12, Irrigated Block, HarvestPro Cotton Station',
 '33.5779N, 101.8552W', 1400.00, NULL, NULL,
 'Planted at optimal soil temperature (65F at 4-inch depth). In-furrow insecticide applied.',
 '2024-05-08', 'Dr. James Thompson', '2024-05-08 10:00:00', '2024-05-08 10:00:00'),

('TRACK033', 'BATCH2024008', 'Squaring', 'Field C-12, Irrigated Block, HarvestPro Cotton Station',
 '33.5779N, 101.8552W', 1400.00, NULL, 9.20,
 'First squares visible. Plant height averaging 14 inches. Excellent node development. No thrips damage.',
 '2024-06-15', 'Dr. James Thompson', '2024-06-15 14:30:00', '2024-06-15 14:30:00'),

('TRACK034', 'BATCH2024008', 'First Bloom', 'Field C-12, Irrigated Block, HarvestPro Cotton Station',
 '33.5779N, 101.8552W', 1400.00, NULL, 9.35,
 'Uniform first bloom. Excellent square retention. Plant mapping shows strong fruit load potential.',
 '2024-07-05', 'Dr. James Thompson', '2024-07-05 09:45:00', '2024-07-05 09:45:00'),

('TRACK035', 'BATCH2024008', 'Peak Bloom', 'Field C-12, Irrigated Block, HarvestPro Cotton Station',
 '33.5779N, 101.8552W', 1400.00, NULL, 9.15,
 'Peak bloom achieved. Boll load excellent. Deficit irrigation treatment initiated for fiber quality enhancement.',
 '2024-07-25', 'Dr. James Thompson', '2024-07-25 11:20:00', '2024-07-25 11:20:00'),

('TRACK036', 'BATCH2024008', 'Boll Development', 'Field C-12, Irrigated Block, HarvestPro Cotton Station',
 '33.5779N, 101.8552W', 1400.00, NULL, 9.25,
 'Excellent boll development. NAWF (nodes above white flower) = 5. Cutout approaching. No bollworm damage.',
 '2024-08-15', 'Dr. James Thompson', '2024-08-15 14:00:00', '2024-08-15 14:00:00'),

('TRACK037', 'BATCH2024008', 'Open Boll and Defoliation', 'Field C-12, Irrigated Block, HarvestPro Cotton Station',
 '33.5779N, 101.8552W', 1400.00, NULL, 9.30,
 'Open boll percentage at 65%. Defoliant applied. Fiber quality samples collected for HVI testing.',
 '2024-10-01', 'Dr. James Thompson', '2024-10-01 10:30:00', '2024-10-01 10:30:00'),

('TRACK038', 'BATCH2024008', 'Harvest', 'Field C-12, Irrigated Block, HarvestPro Cotton Station',
 '33.5779N, 101.8552W', 1400.00, 1485.50, 9.40,
 'Stripper harvest completed. Lint yield 1,485 lb/acre. HVI results: staple 1.22", micronaire 4.0, strength 33.5 g/tex. Premium quality achieved.',
 '2024-10-25', 'Dr. James Thompson', '2024-10-25 17:15:00', '2024-10-25 17:15:00'),

-- ===== Tracking for BATCH2024009 (Malting Barley) =====
('TRACK039', 'BATCH2024009', 'Planting', 'Field B-07, Research Block, Global Seeds Minneapolis',
 '44.9778N, 93.2650W', 3800.00, NULL, NULL,
 'Spring planting at optimal soil conditions. Nitrogen applied at reduced rate for malting quality.',
 '2024-04-18', 'Dr. Michelle Taylor', '2024-04-18 08:45:00', '2024-04-18 08:45:00'),

('TRACK040', 'BATCH2024009', 'Tillering', 'Field B-07, Research Block, Global Seeds Minneapolis',
 '44.9778N, 93.2650W', 3800.00, NULL, 9.40,
 'Excellent tillering at 4.2 tillers/plant. Uniform stand establishment. No early disease pressure.',
 '2024-05-20', 'Dr. Patricia Anderson', '2024-05-20 10:15:00', '2024-05-20 10:15:00'),

('TRACK041', 'BATCH2024009', 'Heading and Flowering', 'Field B-07, Research Block, Global Seeds Minneapolis',
 '44.9778N, 93.2650W', 3800.00, NULL, 9.35,
 'Uniform heading. Excellent spike characteristics. Spot blotch rating 1/9 (highly resistant).',
 '2024-06-18', 'Dr. Michelle Taylor', '2024-06-18 13:30:00', '2024-06-18 13:30:00'),

('TRACK042', 'BATCH2024009', 'Grain Fill', 'Field B-07, Research Block, Global Seeds Minneapolis',
 '44.9778N, 93.2650W', 3800.00, NULL, 9.25,
 'Excellent kernel development. Plump kernel percentage >92%. Protein tracking at target 12.5%.',
 '2024-07-10', 'Dr. Patricia Anderson', '2024-07-10 11:00:00', '2024-07-10 11:00:00'),

('TRACK043', 'BATCH2024009', 'Harvest', 'Field B-07, Research Block, Global Seeds Minneapolis',
 '44.9778N, 93.2650W', 3800.00, 4028.50, 9.45,
 'Harvest at 12.2% moisture. Protein 12.8%. Beta-glucan 5.6%. Germination 97%. Meets all AMBA malting specifications.',
 '2024-08-05', 'Dr. Michelle Taylor', '2024-08-05 16:30:00', '2024-08-05 16:30:00'),

-- ===== Tracking for BATCH2024011 (Grain Sorghum) =====
('TRACK044', 'BATCH2024011', 'Planting', 'Field SG-03, Hybrid Test Block, Global Seeds Texas Station',
 '30.6280N, 96.3344W', 7500.00, NULL, NULL,
 'Planted when soil temperature reached 65F. Seed treatment applied for early-season insect protection.',
 '2024-04-25', 'Dr. David Wilson', '2024-04-25 09:30:00', '2024-04-25 09:30:00'),

('TRACK045', 'BATCH2024011', 'Vegetative Growth', 'Field SG-03, Hybrid Test Block, Global Seeds Texas Station',
 '30.6280N, 96.3344W', 7500.00, NULL, 9.15,
 'Excellent stand establishment. GDU accumulation on track. Sugarcane aphid scouting initiated.',
 '2024-05-30', 'Dr. David Wilson', '2024-05-30 14:00:00', '2024-05-30 14:00:00'),

('TRACK046', 'BATCH2024011', 'Boot and Heading', 'Field SG-03, Hybrid Test Block, Global Seeds Texas Station',
 '30.6280N, 96.3344W', 7500.00, NULL, 9.30,
 'Uniform heading across hybrids. Panicle exertion excellent. Anthracnose rating 2/9 (good resistance).',
 '2024-07-05', 'Dr. David Wilson', '2024-07-05 10:45:00', '2024-07-05 10:45:00'),

('TRACK047', 'BATCH2024011', 'Grain Fill', 'Field SG-03, Hybrid Test Block, Global Seeds Texas Station',
 '30.6280N, 96.3344W', 7500.00, NULL, 9.20,
 'Rapid grain fill. Stay-green trait expressing well in drought-stressed areas. Charcoal rot rating: 1/9.',
 '2024-08-10', 'Dr. David Wilson', '2024-08-10 11:30:00', '2024-08-10 11:30:00'),

('TRACK048', 'BATCH2024011', 'Physiological Maturity and Harvest', 'Field SG-03, Hybrid Test Block, Global Seeds Texas Station',
 '30.6280N, 96.3344W', 7500.00, 8125.75, 9.35,
 'Harvest at 14% moisture. Yield 135 bu/acre (8.3% above target). Test weight 58.2 lb/bu. Excellent hybrid performance.',
 '2024-10-15', 'Dr. David Wilson', '2024-10-15 16:00:00', '2024-10-15 16:00:00'),

-- ===== Tracking for BATCH2024012 (Medium-Grain Rice) =====
('TRACK049', 'BATCH2024012', 'Field Preparation and Flooding', 'Field R-10, Paddy Block A, Global Seeds Delta Farm',
 '34.4671N, 91.5518W', 8500.00, NULL, NULL,
 'Permanent flood established. Levees in good condition. Pre-flood herbicide applied.',
 '2024-04-10', 'Dr. Kevin Park', '2024-04-10 08:00:00', '2024-04-10 08:00:00'),

('TRACK050', 'BATCH2024012', 'Planting (Water-Seeded)', 'Field R-10, Paddy Block A, Global Seeds Delta Farm',
 '34.4671N, 91.5518W', 8500.00, NULL, NULL,
 'Water-seeded at 90 lb/acre. Seed pre-soaked for 24 hours. Excellent seed distribution.',
 '2024-04-15', 'Dr. Kevin Park', '2024-04-15 09:30:00', '2024-04-15 09:30:00'),

('TRACK051', 'BATCH2024012', 'Tillering', 'Field R-10, Paddy Block A, Global Seeds Delta Farm',
 '34.4671N, 91.5518W', 8500.00, NULL, 9.25,
 'Active tillering. Average 5.8 tillers/plant. Flood maintained at 3-4 inches. Nitrogen split applied.',
 '2024-05-25', 'Dr. Kevin Park', '2024-05-25 10:45:00', '2024-05-25 10:45:00'),

('TRACK052', 'BATCH2024012', 'Heading', 'Field R-10, Paddy Block A, Global Seeds Delta Farm',
 '34.4671N, 91.5518W', 8500.00, NULL, 9.35,
 'Uniform heading at 50%. Panicle exertion excellent. Blast scouting shows no lesions on resistant lines.',
 '2024-07-15', 'Dr. Kevin Park', '2024-07-15 14:15:00', '2024-07-15 14:15:00'),

('TRACK053', 'BATCH2024012', 'Grain Fill', 'Field R-10, Paddy Block A, Global Seeds Delta Farm',
 '34.4671N, 91.5518W', 8500.00, NULL, 9.30,
 'Active grain fill. Panicle weight increasing. Chalk content estimates <4%. Milling samples collected.',
 '2024-08-10', 'Dr. Kevin Park', '2024-08-10 11:30:00', '2024-08-10 11:30:00'),

('TRACK054', 'BATCH2024012', 'Drain and Dry-Down', 'Field R-10, Paddy Block A, Global Seeds Delta Farm',
 '34.4671N, 91.5518W', 8500.00, NULL, 9.20,
 'Flood drained for harvest preparation. Grain moisture declining. Field drying well.',
 '2024-09-05', 'Dr. Kevin Park', '2024-09-05 09:00:00', '2024-09-05 09:00:00'),

('TRACK055', 'BATCH2024012', 'Harvest', 'Field R-10, Paddy Block A, Global Seeds Delta Farm',
 '34.4671N, 91.5518W', 8500.00, 8892.50, 9.40,
 'Combine harvest at 18% moisture. Rough rice yield 8,892 lb/acre. Head rice recovery 67%. Chalk 3.2%. Exceeds quality targets.',
 '2024-09-25', 'Dr. Kevin Park', '2024-09-25 17:00:00', '2024-09-25 17:00:00');


SET FOREIGN_KEY_CHECKS = 1;

-- =========================================================================================================
-- DATA SUMMARY
-- =========================================================================================================
-- breeding_plan: 12 records
--   - Corn: 2 plans (hybrid development, drought tolerance)
--   - Wheat: 2 plans (winter wheat, heat-tolerant spring wheat)
--   - Soybean: 2 plans (SCN resistance, high oil)
--   - Oat: 1 plan (organic production)
--   - Cotton: 1 plan (fiber quality)
--   - Barley: 2 plans (malting quality, feed yield)
--   - Sorghum: 1 plan (hybrid development)
--   - Rice: 1 plan (medium-grain quality)
--
-- breeding_material: 30 records
--   - Multiple material types: Inbred Line, F1 Hybrid, Backcross Material,
--     Doubled Haploid, Recombinant Inbred Line, Near-Isogenic Line,
--     Landrace, Elite Germplasm, Foundation/Breeder/Certified Seed
--   - Sources: USDA-ARS, Universities (Iowa State, K-State, Texas A&M, etc.),
--     International centers (CIMMYT), Commercial partners
--
-- breeding_tracking: 55 records
--   - Complete growth stage tracking for major crops
--   - Includes: planting, vegetative stages, flowering, grain fill, harvest
--   - Contains: yield data, disease observations, quality metrics
--   - Field inspection scores ranging from 8.50 to 9.50
-- =========================================================================================================
