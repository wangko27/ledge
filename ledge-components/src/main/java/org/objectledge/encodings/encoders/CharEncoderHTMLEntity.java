package org.objectledge.encodings.encoders;

import java.util.HashMap;

import org.objectledge.encodings.MappingEntry;

/**
 * HTMLEntity encoder.
 *
 * * @author    <a href="mailto:dgajda@caltha.pl">Damian Gajda</a>
 * @version   $Id: CharEncoderHTMLEntity.java,v 1.1 2004-02-02 18:59:00 zwierzem Exp $
 */
public class CharEncoderHTMLEntity
         extends CharEncoder
{

    //---------------------------------------------------------------------
    // static fields

    private static HashMap entitiesByName = new HashMap();

    private static MappingEntry[] entities =
            {
            new MappingEntry(34, "quot"),
            new MappingEntry(38, "amp"),
            new MappingEntry(60, "lt"),
            new MappingEntry(62, "gt"),
            new MappingEntry(160, "nbsp"),
            new MappingEntry(161, "iexcl"),
            new MappingEntry(162, "cent"),
            new MappingEntry(163, "pound"),
            new MappingEntry(164, "curren"),
            new MappingEntry(165, "yen"),
            new MappingEntry(166, "brvbar"),
            new MappingEntry(167, "sect"),
            new MappingEntry(168, "uml"),
            new MappingEntry(169, "copy"),
            new MappingEntry(170, "ordf"),
            new MappingEntry(171, "laquo"),
            new MappingEntry(172, "not"),
            new MappingEntry(173, "shy"),
            new MappingEntry(174, "reg"),
            new MappingEntry(175, "macr"),
            new MappingEntry(176, "deg"),
            new MappingEntry(177, "plusmn"),
            new MappingEntry(178, "sup2"),
            new MappingEntry(179, "sup3"),
            new MappingEntry(180, "acute"),
            new MappingEntry(181, "micro"),
            new MappingEntry(182, "para"),
            new MappingEntry(183, "middot"),
            new MappingEntry(184, "cedil"),
            new MappingEntry(185, "sup1"),
            new MappingEntry(186, "ordm"),
            new MappingEntry(187, "raquo"),
            new MappingEntry(188, "frac14"),
            new MappingEntry(189, "frac12"),
            new MappingEntry(190, "frac34"),
            new MappingEntry(191, "iquest"),
            new MappingEntry(192, "Agrave"),
            new MappingEntry(193, "Aacute"),
            new MappingEntry(194, "Acirc"),
            new MappingEntry(195, "Atilde"),
            new MappingEntry(196, "Auml"),
            new MappingEntry(197, "Aring"),
            new MappingEntry(198, "AElig"),
            new MappingEntry(199, "Ccedil"),
            new MappingEntry(200, "Egrave"),
            new MappingEntry(201, "Eacute"),
            new MappingEntry(202, "Ecirc"),
            new MappingEntry(203, "Euml"),
            new MappingEntry(204, "Igrave"),
            new MappingEntry(205, "Iacute"),
            new MappingEntry(206, "Icirc"),
            new MappingEntry(207, "Iuml"),
            new MappingEntry(208, "ETH"),
            new MappingEntry(209, "Ntilde"),
            new MappingEntry(210, "Ograve"),
            new MappingEntry(211, "Oacute"),
            new MappingEntry(212, "Ocirc"),
            new MappingEntry(213, "Otilde"),
            new MappingEntry(214, "Ouml"),
            new MappingEntry(215, "times"),
            new MappingEntry(216, "Oslash"),
            new MappingEntry(217, "Ugrave"),
            new MappingEntry(218, "Uacute"),
            new MappingEntry(219, "Ucirc"),
            new MappingEntry(220, "Uuml"),
            new MappingEntry(221, "Yacute"),
            new MappingEntry(222, "THORN"),
            new MappingEntry(223, "szlig"),
            new MappingEntry(224, "agrave"),
            new MappingEntry(225, "aacute"),
            new MappingEntry(226, "acirc"),
            new MappingEntry(227, "atilde"),
            new MappingEntry(228, "auml"),
            new MappingEntry(229, "aring"),
            new MappingEntry(230, "aelig"),
            new MappingEntry(231, "ccedil"),
            new MappingEntry(232, "egrave"),
            new MappingEntry(233, "eacute"),
            new MappingEntry(234, "ecirc"),
            new MappingEntry(235, "euml"),
            new MappingEntry(236, "igrave"),
            new MappingEntry(237, "iacute"),
            new MappingEntry(238, "icirc"),
            new MappingEntry(239, "iuml"),
            new MappingEntry(240, "eth"),
            new MappingEntry(241, "ntilde"),
            new MappingEntry(242, "ograve"),
            new MappingEntry(243, "oacute"),
            new MappingEntry(244, "ocirc"),
            new MappingEntry(245, "otilde"),
            new MappingEntry(246, "ouml"),
            new MappingEntry(247, "divide"),
            new MappingEntry(248, "oslash"),
            new MappingEntry(249, "ugrave"),
            new MappingEntry(250, "uacute"),
            new MappingEntry(251, "ucirc"),
            new MappingEntry(252, "uuml"),
            new MappingEntry(253, "yacute"),
            new MappingEntry(254, "thorn"),
            new MappingEntry(255, "yuml"),
            new MappingEntry(338, "OElig"),
            new MappingEntry(339, "oelig"),
            new MappingEntry(352, "Scaron"),
            new MappingEntry(353, "scaron"),
            new MappingEntry(376, "Yuml"),
            new MappingEntry(402, "fnof"),
            new MappingEntry(710, "circ"),
            new MappingEntry(732, "tilde"),
            new MappingEntry(913, "Alpha"),
            new MappingEntry(914, "Beta"),
            new MappingEntry(915, "Gamma"),
            new MappingEntry(916, "Delta"),
            new MappingEntry(917, "Epsilon"),
            new MappingEntry(918, "Zeta"),
            new MappingEntry(919, "Eta"),
            new MappingEntry(920, "Theta"),
            new MappingEntry(921, "Iota"),
            new MappingEntry(922, "Kappa"),
            new MappingEntry(923, "Lambda"),
            new MappingEntry(924, "Mu"),
            new MappingEntry(925, "Nu"),
            new MappingEntry(926, "Xi"),
            new MappingEntry(927, "Omicron"),
            new MappingEntry(928, "Pi"),
            new MappingEntry(929, "Rho"),
            new MappingEntry(931, "Sigma"),
            new MappingEntry(932, "Tau"),
            new MappingEntry(933, "Upsilon"),
            new MappingEntry(934, "Phi"),
            new MappingEntry(935, "Chi"),
            new MappingEntry(936, "Psi"),
            new MappingEntry(937, "Omega"),
            new MappingEntry(945, "alpha"),
            new MappingEntry(946, "beta"),
            new MappingEntry(947, "gamma"),
            new MappingEntry(948, "delta"),
            new MappingEntry(949, "epsilon"),
            new MappingEntry(950, "zeta"),
            new MappingEntry(951, "eta"),
            new MappingEntry(952, "theta"),
            new MappingEntry(953, "iota"),
            new MappingEntry(954, "kappa"),
            new MappingEntry(955, "lambda"),
            new MappingEntry(956, "mu"),
            new MappingEntry(957, "nu"),
            new MappingEntry(958, "xi"),
            new MappingEntry(959, "omicron"),
            new MappingEntry(960, "pi"),
            new MappingEntry(961, "rho"),
            new MappingEntry(962, "sigmaf"),
            new MappingEntry(963, "sigma"),
            new MappingEntry(964, "tau"),
            new MappingEntry(965, "upsilon"),
            new MappingEntry(966, "phi"),
            new MappingEntry(967, "chi"),
            new MappingEntry(968, "psi"),
            new MappingEntry(969, "omega"),
            new MappingEntry(977, "thetasym"),
            new MappingEntry(978, "upsih"),
            new MappingEntry(982, "piv"),
            new MappingEntry(8194, "ensp"),
            new MappingEntry(8195, "emsp"),
            new MappingEntry(8201, "thinsp"),
            new MappingEntry(8204, "zwnj"),
            new MappingEntry(8205, "zwj"),
            new MappingEntry(8206, "lrm"),
            new MappingEntry(8207, "rlm"),
            new MappingEntry(8211, "ndash"),
            new MappingEntry(8212, "mdash"),
            new MappingEntry(8216, "lsquo"),
            new MappingEntry(8217, "rsquo"),
            new MappingEntry(8218, "sbquo"),
            new MappingEntry(8220, "ldquo"),
            new MappingEntry(8221, "rdquo"),
            new MappingEntry(8222, "bdquo"),
            new MappingEntry(8224, "dagger"),
            new MappingEntry(8225, "Dagger"),
            new MappingEntry(8226, "bull"),
            new MappingEntry(8230, "hellip"),
            new MappingEntry(8240, "permil"),
            new MappingEntry(8242, "prime"),
            new MappingEntry(8243, "Prime"),
            new MappingEntry(8249, "lsaquo"),
            new MappingEntry(8250, "rsaquo"),
            new MappingEntry(8254, "oline"),
            new MappingEntry(8260, "frasl"),
            new MappingEntry(8364, "euro"),
            new MappingEntry(8465, "image"),
            new MappingEntry(8472, "weierp"),
            new MappingEntry(8476, "real"),
            new MappingEntry(8482, "trade"),
            new MappingEntry(8501, "alefsym"),
            new MappingEntry(8592, "larr"),
            new MappingEntry(8593, "uarr"),
            new MappingEntry(8594, "rarr"),
            new MappingEntry(8595, "darr"),
            new MappingEntry(8596, "harr"),
            new MappingEntry(8629, "crarr"),
            new MappingEntry(8656, "lArr"),
            new MappingEntry(8657, "uArr"),
            new MappingEntry(8658, "rArr"),
            new MappingEntry(8659, "dArr"),
            new MappingEntry(8660, "hArr"),
            new MappingEntry(8704, "forall"),
            new MappingEntry(8706, "part"),
            new MappingEntry(8707, "exist"),
            new MappingEntry(8709, "empty"),
            new MappingEntry(8711, "nabla"),
            new MappingEntry(8712, "isin"),
            new MappingEntry(8713, "notin"),
            new MappingEntry(8715, "ni"),
            new MappingEntry(8719, "prod"),
            new MappingEntry(8721, "sum"),
            new MappingEntry(8722, "minus"),
            new MappingEntry(8727, "lowast"),
            new MappingEntry(8730, "radic"),
            new MappingEntry(8733, "prop"),
            new MappingEntry(8734, "infin"),
            new MappingEntry(8736, "ang"),
            new MappingEntry(8743, "and"),
            new MappingEntry(8744, "or"),
            new MappingEntry(8745, "cap"),
            new MappingEntry(8746, "cup"),
            new MappingEntry(8747, "int"),
            new MappingEntry(8756, "there4"),
            new MappingEntry(8764, "sim"),
            new MappingEntry(8773, "cong"),
            new MappingEntry(8776, "asymp"),
            new MappingEntry(8800, "ne"),
            new MappingEntry(8801, "equiv"),
            new MappingEntry(8804, "le"),
            new MappingEntry(8805, "ge"),
            new MappingEntry(8834, "sub"),
            new MappingEntry(8835, "sup"),
            new MappingEntry(8836, "nsub"),
            new MappingEntry(8838, "sube"),
            new MappingEntry(8839, "supe"),
            new MappingEntry(8853, "oplus"),
            new MappingEntry(8855, "otimes"),
            new MappingEntry(8869, "perp"),
            new MappingEntry(8901, "sdot"),
            new MappingEntry(8968, "lceil"),
            new MappingEntry(8969, "rceil"),
            new MappingEntry(8970, "lfloor"),
            new MappingEntry(8971, "rfloor"),
            new MappingEntry(9001, "lang"),
            new MappingEntry(9002, "rang"),
            new MappingEntry(9674, "loz"),
            new MappingEntry(9824, "spades"),
            new MappingEntry(9827, "clubs"),
            new MappingEntry(9829, "hearts"),
            new MappingEntry(9830, "diams")
            };


    /** Array indexed by characters most significant byte.
     *  It contains indexes for a second table containing values mapped. */
    private static final int[] PREFIX_INDEX = {
0x0, 0x100, 0x200, 0x300, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0x400, 0x500, 0x600, 0x700, 0xa00, 0x800, 0x900, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00,
0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00, 0xa00
    };

    /** Array indexed both by prefixIndex array and characters least significant byte.
     *  It contains values mapped onto characters. */
    private static final char[][] SUFFIX_INDEX = {
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,{ 0x71, 0x75, 0x6f, 0x74 },null,null,null,{ 0x61, 0x6d, 0x70 },{ 0x61, 0x70, 0x6f, 0x73 },
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,{ 0x6c, 0x74 },null,{ 0x67, 0x74 },null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x6e, 0x62, 0x73, 0x70 },{ 0x69, 0x65, 0x78, 0x63, 0x6c },{ 0x63, 0x65, 0x6e, 0x74 },{ 0x70, 0x6f, 0x75, 0x6e, 0x64 },{ 0x63, 0x75, 0x72, 0x72, 0x65, 0x6e },{ 0x79, 0x65, 0x6e },{ 0x62, 0x72, 0x76, 0x62, 0x61, 0x72 },{ 0x73, 0x65, 0x63, 0x74 },
{ 0x75, 0x6d, 0x6c },{ 0x63, 0x6f, 0x70, 0x79 },{ 0x6f, 0x72, 0x64, 0x66 },{ 0x6c, 0x61, 0x71, 0x75, 0x6f },{ 0x6e, 0x6f, 0x74 },{ 0x73, 0x68, 0x79 },{ 0x72, 0x65, 0x67 },{ 0x6d, 0x61, 0x63, 0x72 },
{ 0x64, 0x65, 0x67 },{ 0x70, 0x6c, 0x75, 0x73, 0x6d, 0x6e },{ 0x73, 0x75, 0x70, 0x32 },{ 0x73, 0x75, 0x70, 0x33 },{ 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x6d, 0x69, 0x63, 0x72, 0x6f },{ 0x70, 0x61, 0x72, 0x61 },{ 0x6d, 0x69, 0x64, 0x64, 0x6f, 0x74 },
{ 0x63, 0x65, 0x64, 0x69, 0x6c },{ 0x73, 0x75, 0x70, 0x31 },{ 0x6f, 0x72, 0x64, 0x6d },{ 0x72, 0x61, 0x71, 0x75, 0x6f },{ 0x66, 0x72, 0x61, 0x63, 0x31, 0x34 },{ 0x66, 0x72, 0x61, 0x63, 0x31, 0x32 },{ 0x66, 0x72, 0x61, 0x63, 0x33, 0x34 },{ 0x69, 0x71, 0x75, 0x65, 0x73, 0x74 },
{ 0x41, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x41, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x41, 0x63, 0x69, 0x72, 0x63 },{ 0x41, 0x74, 0x69, 0x6c, 0x64, 0x65 },{ 0x41, 0x75, 0x6d, 0x6c },{ 0x41, 0x72, 0x69, 0x6e, 0x67 },{ 0x41, 0x45, 0x6c, 0x69, 0x67 },{ 0x43, 0x63, 0x65, 0x64, 0x69, 0x6c },
{ 0x45, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x45, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x45, 0x63, 0x69, 0x72, 0x63 },{ 0x45, 0x75, 0x6d, 0x6c },{ 0x49, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x49, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x49, 0x63, 0x69, 0x72, 0x63 },{ 0x49, 0x75, 0x6d, 0x6c },
{ 0x45, 0x54, 0x48 },{ 0x4e, 0x74, 0x69, 0x6c, 0x64, 0x65 },{ 0x4f, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x4f, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x4f, 0x63, 0x69, 0x72, 0x63 },{ 0x4f, 0x74, 0x69, 0x6c, 0x64, 0x65 },{ 0x4f, 0x75, 0x6d, 0x6c },{ 0x74, 0x69, 0x6d, 0x65, 0x73 },
{ 0x4f, 0x73, 0x6c, 0x61, 0x73, 0x68 },{ 0x55, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x55, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x55, 0x63, 0x69, 0x72, 0x63 },{ 0x55, 0x75, 0x6d, 0x6c },{ 0x59, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x54, 0x48, 0x4f, 0x52, 0x4e },{ 0x73, 0x7a, 0x6c, 0x69, 0x67 },
{ 0x61, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x61, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x61, 0x63, 0x69, 0x72, 0x63 },{ 0x61, 0x74, 0x69, 0x6c, 0x64, 0x65 },{ 0x61, 0x75, 0x6d, 0x6c },{ 0x61, 0x72, 0x69, 0x6e, 0x67 },{ 0x61, 0x65, 0x6c, 0x69, 0x67 },{ 0x63, 0x63, 0x65, 0x64, 0x69, 0x6c },
{ 0x65, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x65, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x65, 0x63, 0x69, 0x72, 0x63 },{ 0x65, 0x75, 0x6d, 0x6c },{ 0x69, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x69, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x69, 0x63, 0x69, 0x72, 0x63 },{ 0x69, 0x75, 0x6d, 0x6c },
{ 0x65, 0x74, 0x68 },{ 0x6e, 0x74, 0x69, 0x6c, 0x64, 0x65 },{ 0x6f, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x6f, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x6f, 0x63, 0x69, 0x72, 0x63 },{ 0x6f, 0x74, 0x69, 0x6c, 0x64, 0x65 },{ 0x6f, 0x75, 0x6d, 0x6c },{ 0x64, 0x69, 0x76, 0x69, 0x64, 0x65 },
{ 0x6f, 0x73, 0x6c, 0x61, 0x73, 0x68 },{ 0x75, 0x67, 0x72, 0x61, 0x76, 0x65 },{ 0x75, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x75, 0x63, 0x69, 0x72, 0x63 },{ 0x75, 0x75, 0x6d, 0x6c },{ 0x79, 0x61, 0x63, 0x75, 0x74, 0x65 },{ 0x74, 0x68, 0x6f, 0x72, 0x6e },{ 0x79, 0x75, 0x6d, 0x6c },
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,{ 0x4f, 0x45, 0x6c, 0x69, 0x67 },{ 0x6f, 0x65, 0x6c, 0x69, 0x67 },null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x53, 0x63, 0x61, 0x72, 0x6f, 0x6e },{ 0x73, 0x63, 0x61, 0x72, 0x6f, 0x6e },null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x59, 0x75, 0x6d, 0x6c },null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,{ 0x66, 0x6e, 0x6f, 0x66 },null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,{ 0x63, 0x69, 0x72, 0x63 },null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,{ 0x74, 0x69, 0x6c, 0x64, 0x65 },null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,{ 0x41, 0x6c, 0x70, 0x68, 0x61 },{ 0x42, 0x65, 0x74, 0x61 },{ 0x47, 0x61, 0x6d, 0x6d, 0x61 },{ 0x44, 0x65, 0x6c, 0x74, 0x61 },{ 0x45, 0x70, 0x73, 0x69, 0x6c, 0x6f, 0x6e },{ 0x5a, 0x65, 0x74, 0x61 },{ 0x45, 0x74, 0x61 },
{ 0x54, 0x68, 0x65, 0x74, 0x61 },{ 0x49, 0x6f, 0x74, 0x61 },{ 0x4b, 0x61, 0x70, 0x70, 0x61 },{ 0x4c, 0x61, 0x6d, 0x62, 0x64, 0x61 },{ 0x4d, 0x75 },{ 0x4e, 0x75 },{ 0x58, 0x69 },{ 0x4f, 0x6d, 0x69, 0x63, 0x72, 0x6f, 0x6e },
{ 0x50, 0x69 },{ 0x52, 0x68, 0x6f },null,{ 0x53, 0x69, 0x67, 0x6d, 0x61 },{ 0x54, 0x61, 0x75 },{ 0x55, 0x70, 0x73, 0x69, 0x6c, 0x6f, 0x6e },{ 0x50, 0x68, 0x69 },{ 0x43, 0x68, 0x69 },
{ 0x50, 0x73, 0x69 },{ 0x4f, 0x6d, 0x65, 0x67, 0x61 },null,null,null,null,null,null,
null,{ 0x61, 0x6c, 0x70, 0x68, 0x61 },{ 0x62, 0x65, 0x74, 0x61 },{ 0x67, 0x61, 0x6d, 0x6d, 0x61 },{ 0x64, 0x65, 0x6c, 0x74, 0x61 },{ 0x65, 0x70, 0x73, 0x69, 0x6c, 0x6f, 0x6e },{ 0x7a, 0x65, 0x74, 0x61 },{ 0x65, 0x74, 0x61 },
{ 0x74, 0x68, 0x65, 0x74, 0x61 },{ 0x69, 0x6f, 0x74, 0x61 },{ 0x6b, 0x61, 0x70, 0x70, 0x61 },{ 0x6c, 0x61, 0x6d, 0x62, 0x64, 0x61 },{ 0x6d, 0x75 },{ 0x6e, 0x75 },{ 0x78, 0x69 },{ 0x6f, 0x6d, 0x69, 0x63, 0x72, 0x6f, 0x6e },
{ 0x70, 0x69 },{ 0x72, 0x68, 0x6f },{ 0x73, 0x69, 0x67, 0x6d, 0x61, 0x66 },{ 0x73, 0x69, 0x67, 0x6d, 0x61 },{ 0x74, 0x61, 0x75 },{ 0x75, 0x70, 0x73, 0x69, 0x6c, 0x6f, 0x6e },{ 0x70, 0x68, 0x69 },{ 0x63, 0x68, 0x69 },
{ 0x70, 0x73, 0x69 },{ 0x6f, 0x6d, 0x65, 0x67, 0x61 },null,null,null,null,null,null,
null,{ 0x74, 0x68, 0x65, 0x74, 0x61, 0x73, 0x79, 0x6d },{ 0x75, 0x70, 0x73, 0x69, 0x68 },null,null,null,{ 0x70, 0x69, 0x76 },null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,{ 0x65, 0x6e, 0x73, 0x70 },{ 0x65, 0x6d, 0x73, 0x70 },null,null,null,null,
null,{ 0x74, 0x68, 0x69, 0x6e, 0x73, 0x70 },null,null,{ 0x7a, 0x77, 0x6e, 0x6a },{ 0x7a, 0x77, 0x6a },{ 0x6c, 0x72, 0x6d },{ 0x72, 0x6c, 0x6d },
null,null,null,{ 0x6e, 0x64, 0x61, 0x73, 0x68 },{ 0x6d, 0x64, 0x61, 0x73, 0x68 },null,null,null,
{ 0x6c, 0x73, 0x71, 0x75, 0x6f },{ 0x72, 0x73, 0x71, 0x75, 0x6f },{ 0x73, 0x62, 0x71, 0x75, 0x6f },null,{ 0x6c, 0x64, 0x71, 0x75, 0x6f },{ 0x72, 0x64, 0x71, 0x75, 0x6f },{ 0x62, 0x64, 0x71, 0x75, 0x6f },null,
{ 0x64, 0x61, 0x67, 0x67, 0x65, 0x72 },{ 0x44, 0x61, 0x67, 0x67, 0x65, 0x72 },{ 0x62, 0x75, 0x6c, 0x6c },null,null,null,{ 0x68, 0x65, 0x6c, 0x6c, 0x69, 0x70 },null,
null,null,null,null,null,null,null,null,
{ 0x70, 0x65, 0x72, 0x6d, 0x69, 0x6c },null,{ 0x70, 0x72, 0x69, 0x6d, 0x65 },{ 0x50, 0x72, 0x69, 0x6d, 0x65 },null,null,null,null,
null,{ 0x6c, 0x73, 0x61, 0x71, 0x75, 0x6f },{ 0x72, 0x73, 0x61, 0x71, 0x75, 0x6f },null,null,null,{ 0x6f, 0x6c, 0x69, 0x6e, 0x65 },null,
null,null,null,null,{ 0x66, 0x72, 0x61, 0x73, 0x6c },null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,{ 0x65, 0x75, 0x72, 0x6f },null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,{ 0x69, 0x6d, 0x61, 0x67, 0x65 },null,null,null,null,null,null,
{ 0x77, 0x65, 0x69, 0x65, 0x72, 0x70 },null,null,null,{ 0x72, 0x65, 0x61, 0x6c },null,null,null,
null,null,{ 0x74, 0x72, 0x61, 0x64, 0x65 },null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,{ 0x61, 0x6c, 0x65, 0x66, 0x73, 0x79, 0x6d },null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x6c, 0x61, 0x72, 0x72 },{ 0x75, 0x61, 0x72, 0x72 },{ 0x72, 0x61, 0x72, 0x72 },{ 0x64, 0x61, 0x72, 0x72 },{ 0x68, 0x61, 0x72, 0x72 },null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,{ 0x63, 0x72, 0x61, 0x72, 0x72 },null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x6c, 0x41, 0x72, 0x72 },{ 0x75, 0x41, 0x72, 0x72 },{ 0x72, 0x41, 0x72, 0x72 },{ 0x64, 0x41, 0x72, 0x72 },{ 0x68, 0x41, 0x72, 0x72 },null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x66, 0x6f, 0x72, 0x61, 0x6c, 0x6c },null,{ 0x70, 0x61, 0x72, 0x74 },{ 0x65, 0x78, 0x69, 0x73, 0x74 },null,{ 0x65, 0x6d, 0x70, 0x74, 0x79 },null,{ 0x6e, 0x61, 0x62, 0x6c, 0x61 },
{ 0x69, 0x73, 0x69, 0x6e },{ 0x6e, 0x6f, 0x74, 0x69, 0x6e },null,{ 0x6e, 0x69 },null,null,null,{ 0x70, 0x72, 0x6f, 0x64 },
null,{ 0x73, 0x75, 0x6d },{ 0x6d, 0x69, 0x6e, 0x75, 0x73 },null,null,null,null,{ 0x6c, 0x6f, 0x77, 0x61, 0x73, 0x74 },
null,null,{ 0x72, 0x61, 0x64, 0x69, 0x63 },null,null,{ 0x70, 0x72, 0x6f, 0x70 },{ 0x69, 0x6e, 0x66, 0x69, 0x6e },null,
{ 0x61, 0x6e, 0x67 },null,null,null,null,null,null,{ 0x61, 0x6e, 0x64 },
{ 0x6f, 0x72 },{ 0x63, 0x61, 0x70 },{ 0x63, 0x75, 0x70 },{ 0x69, 0x6e, 0x74 },null,null,null,null,
null,null,null,null,{ 0x74, 0x68, 0x65, 0x72, 0x65, 0x34 },null,null,null,
null,null,null,null,{ 0x73, 0x69, 0x6d },null,null,null,
null,null,null,null,null,{ 0x63, 0x6f, 0x6e, 0x67 },null,null,
{ 0x61, 0x73, 0x79, 0x6d, 0x70 },null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x6e, 0x65 },{ 0x65, 0x71, 0x75, 0x69, 0x76 },null,null,{ 0x6c, 0x65 },{ 0x67, 0x65 },null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,{ 0x73, 0x75, 0x62 },{ 0x73, 0x75, 0x70 },{ 0x6e, 0x73, 0x75, 0x62 },null,{ 0x73, 0x75, 0x62, 0x65 },{ 0x73, 0x75, 0x70, 0x65 },
null,null,null,null,null,null,null,null,
null,null,null,null,null,{ 0x6f, 0x70, 0x6c, 0x75, 0x73 },null,{ 0x6f, 0x74, 0x69, 0x6d, 0x65, 0x73 },
null,null,null,null,null,null,null,null,
null,null,null,null,null,{ 0x70, 0x65, 0x72, 0x70 },null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,{ 0x73, 0x64, 0x6f, 0x74 },null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x6c, 0x63, 0x65, 0x69, 0x6c },{ 0x72, 0x63, 0x65, 0x69, 0x6c },{ 0x6c, 0x66, 0x6c, 0x6f, 0x6f, 0x72 },{ 0x72, 0x66, 0x6c, 0x6f, 0x6f, 0x72 },null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,{ 0x6c, 0x61, 0x6e, 0x67 },{ 0x72, 0x61, 0x6e, 0x67 },null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,{ 0x6c, 0x6f, 0x7a },null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
{ 0x73, 0x70, 0x61, 0x64, 0x65, 0x73 },null,null,{ 0x63, 0x6c, 0x75, 0x62, 0x73 },null,{ 0x68, 0x65, 0x61, 0x72, 0x74, 0x73 },{ 0x64, 0x69, 0x61, 0x6d, 0x73 },null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null
    };

    static
    {
        for(int i = 0; i < entities.length; i++)
        {
            String key = new String(entities[i].getValue());
            entitiesByName.put(key, entities[i]);
        }
    }


    /** Constructor for the CharEncoderHTMLEntity object */
    public CharEncoderHTMLEntity()
    {
        ((CharEncoder)this).prefixIndex = PREFIX_INDEX;
        ((CharEncoder)this).suffixIndex = SUFFIX_INDEX;
    }


    /**
     * Translates an entity name to its unicode value.
     * Entity names may be in string or numerical form.
     *
     * @param name  Name of an entity with &amp; or &amp;# at the begining and ; at the end
     * @return      Returns entity unicode code or zero on error
     */
    public short entityCode(String name)
    {
        int c;

        if(name.length() <= 1)
        {
            return 0;
        }

        /* numeric entitity: name = "&#" followed by number */
        if(name.charAt(1) == '#')
        {
            c = 0;
            /* zero on missing/bad number */
            /* 'x' prefix denotes hexadecimal number format */
            try
            {
                if(name.length() >= 4 && name.charAt(2) == 'x')
                {
                    c = Integer.parseInt(name.substring(3), 16);
                }
                else if(name.length() >= 3)
                {
                    c = Integer.parseInt(name.substring(2));
                }
            }
            catch(NumberFormatException e)
            {
            }

            return (short)c;
        }

        /* Named entity: name ="&" followed by a name */
        MappingEntry ent = (MappingEntry)entitiesByName.get(name.substring(1));
        if(ent != null)
        {
            return ent.getUnicodeCode();
        }

        return 0; /* zero signifies unknown entity name */
    }
}

