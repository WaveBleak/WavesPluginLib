package dk.wavebleak.wavespluginlib.labymodhelpers;

import lombok.Getter;

@Getter
public enum EnumEmoteType {
    STOP_EMOTE(-1),
    Backflip(2),
    Dab(3),
    Hello(4),
    Bow_thanks(5),
    Hype(6),
    Tryingtofly(7),
    Infinity_sit(8),
    Zombie(11),
    Hula_Hoop(13),
    Calling(14),
    Facepalm(15),
    Brush_your_shoulders(18),
    Split(19),
    Salute(20),
    Balarina(22),
    Handstand(31),
    Helicopter(32),
    Holy(33),
    Waveover(34),
    Deeper_deeper(36),
    Karate(37),
    Moonwalk(38),
    Freezing(40),
    Jubilation(41),
    Turtle(43),
    Headspin(45),
    Infinity_Dab(46),
    Chicken(47),
    The_Floss(49),
    The_mega_thrust(50),
    The_cleaner(51),
    Bridge(52),
    Milk_the_cow(53),
    Rurik(54),
    Wave(55),
    Money_rain(57),
    The_pointer(59),
    Frightening(60),
    Sad(61),
    Air_guitar(62),
    Witch(63),
    Left(69),
    Right(70),
    Buuuh(74),
    Spitting_bars(75),
    Count_money(76),
    Hug(77),
    Applause(78),
    Boxing(79),
    Shoot(83),
    The_pointing_man(84),
    Heart(85),
    Near_the_fall(86),
    Waiting(89),
    Praise_your_item(92),
    Look(93),
    I_love_you(97),
    Sarcastic_clap(98),
    You(101),
    Head_on_wall(105),
    Balance(112),
    Levelup(113),
    Take_the_L(114),
    My_idol(121),
    Airplane(122),
    Eagle(124),
    Job_well_done(126),
    Elephant(128),
    Present(130),
    Eyes_on_you(131),
    Bow_down(133),
    Maneki_neko(134),
    Conductor(135),
    Didi_challenge(136),
    Snow_Angel(137),
    Snowball(138),
    Sprinkler(139),
    Calculated(140),
    One_armed_handstand(141),
    Eat(142),
    Shy(143),
    Sit_Ups(145),
    Breakdance(146),
    Mindblow(148),
    Fall(149),
    T_Pose(150),
    Jumping_Jack(153),
    Backstroke(154),
    Ice_Hockey(156),
    Look_at_fireworks(157),
    Finish_the_tree(158),
    Ice_Skating(159),
    Fancy_Feet(161),
    Ronaldo(162),
    True_Heart(163),
    Pumpernickel(164),
    Baby_Shark(166),
    Open_present(167),
    Dj(170),
    Have_to_pee(171),
    Sneeze(173),
    Cheerleader(178),
    Naruto_Run(180),
    Pati_Patu(181),
    Axe_Swing(182),
    Fusion_Left(183),
    Fishing(184),
    Fusion_Right(185),
    Breathless(187),
    Genkidama(189),
    Singer(191),
    Magikarp(192),
    Rage(193),
    Slap(194),
    Air_kisses(195),
    Knockout(196),
    Matrix(197),
    Jetpack(198),
    Golf(200),
    Stadium_wave(201),
    Kickboxer(202),
    Handshake(203),
    Cleaning_the_floor(204),
    Sweat_Wipe(209),
    Hokuspokus(213),
    The_Bat(215);

    private final int id;

    EnumEmoteType(int id) {
        this.id = id;
    }
}