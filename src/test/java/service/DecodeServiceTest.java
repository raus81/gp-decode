package service;

import model.CertificateModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecodeServiceTest {
    public static final String C_GAMMA_STORE = "c:/gamma/store";
    private final String testStringFirstDose = "HC1:6BFOXN%TS3DHPVO13J /G-/2YRVA.Q/R8OJE2FC1J9M$DI9C3K92AFTU0*MIN9HNO4*J8OX4W$C2VL*LA 43/IE%TE6UG+ZEAT1HQ13W1:O1YUI%F1PN1/T1%%HRP5 R14SI.J9DYHZRO$$0ORNYZ00OP748$NI4L6E QSO1JL83ZBGS9W.J*X1QO3YY9O$RKWHR$P1R53/J3T90Q3KUB/T1%%PD 9ZIEQKERQ8IY1I$HH%U8 9PS5OH6RWPOP6OH6MN96VHDV22JA.IEGW2TK2TH9YJA-LH/CJTK96L6SR9MU93FGRRJ/9TL4T1C9 UP2LP1+1QN1RNUM/U0T9FVPIAEYZQ4H9R%R5 BQQGZ/J.UIH$UT.TFRMLNKNM8JI0EUGP$I/XK$M86L1+WT9UEZV5PO3QX6%%9JHH3%QM5S/G98VV6R7OPUE3KRYP76VBW1DKRVBCG9LHM9EZ4SETCQT1$NI$3FOJ-A00ANWIE %34ADQS26RF+GGNIS-*IR10N4Q:0";
    private final String testStringSecondDose = "HC1:6BFOXN%TS3DHPVO13J /G-/2YRVA.Q/R8OJE2FC1J9M$DI9CTI90UKULIRJPC%OQHIZC4.OI1RM8ZA.A5:S9MKN4NN3F85QNCY0O%0VZ001HOC9JU0D0HT0HB2PL/IB*09B9LW4T*8+DCSJ0PZB:WO$*SBAKYE9*FJ7ID$0HL94A3LU:CE 4W%8L:DQ*985TT3Q 0I30J41L81H63P6*5T:D.7T:0LPHN6D7LLK*2HG%89UV-0LZ 2ZJJ %C4IJZJJBY43%8 C1VHLEC78G1TFHM*K2ILS-O:S9UZ4+FJE 4Y3LO78L:P WUQRENS431T8UCN.06$0/8F-TV%*4M/S2879NT2V4T:DP+5DD8P7QLF9%FF$/EUDBQEAJJKHHGEC8ZI9$JAQJK3X0C:U5QNW/ID 1V6H%EW-*5H84.7E/57%MEQ.I.IF5K49ERNGN9N0V-TG/J4ACB1WICSUV49K1N%DYGMEAT*A6+4P8$P /JZFCB6MAVAG:QKOBQ6V$*0Q%A4BF";

    private final String testRecovery = "HC1:NCFOXN%TSMAHN-H3ZSUZK+.V0ET9%6-AH-XI1ROR$SIOOV*ILLELRHKG5TTUG%5F/8X*G3M9FQH+4JZW4Z*AK.GNNVR*G0C7PHBO33BC7R7BWBJ+47RK3X77JD7IHJMOJCRN%BB%KNEB32NJND3XSJBG71MJRPFSZ4ZI00T9 E9PF6846A$Q436BPKZXI551DDQV ONM5WOP4LT1833ZCO1B8ZVV5TN%2UP20J5/5LEBFD-48YIWFT-67-C0*E6-RI PQVW5/O16%HAT1Z%PPRAAUICO10W5:EOL:P WUQRELS431TCRVJ0R$%2DU2O3J$NNP5SLAFG.CILFSCA6LF20HFJC3DAYJDPKD4JB2E9Z3E8AE-QD+PB.QCD-H/8O3BEQ8L9VNT7A6LFCD9KWNHPA%8L+5INKE$JDVPL32K01K%XGV4PSAO*D12YH5QF:.98M7J$V$+IHQI5NAEQRFSQ1T2S28 QPGA723TV/NQRMR3M153$+9SRJ0 ADY5NYDB3WH6UJ/J+:RX%6ZBWQXA8T7MKG";

    private final String testMolecularTest = "HC1:NCFPY3008IK3600EAE69H9TR7M8O*G8J87/8%SF9TF-U3Z:B2:UGQ49$50+MM 5+:J*5F%P4GAF-F9W3H2OHNRSRGT6M5AADZSHGCK7HK%G7ZH9:*0D.2609PJIZYOJ S.CV$.4W-OT:SKDRLNRU/UH2C2QFBTJGX2TRK3ZPZTTHTURLN2CP6JVG66/ OW/KNXPI4ECRU68BK395QP7*5VD0NKB6KJ2T1L.LT+73W5AVGAYLJBJ4LRNTOM0LHEP5BO.M2AAH4ROXO6 RH1JUWXHD/Q9YA3/C1 9K7MCSLY15L05P5SYJ3BVC1EGKKK+0Q0G3A-QKMQ4P9SSAEAG/F2%O48$3--CXM6/R7JU4YITJ4L9F4U85EI616I5*417MMD4M3KVBGKE63DO$3TWY03 QN103SP666FZ6UV4VJ7X24HCKPTCQUCX%G%CUJOLNY2+:8UNLY.F6LT-/T7*FZ-0+Q8082Y+7H77YA6JX2H$AEXITXRHXS1*IC7LO8J-LVW9P2DD2YCR7BG07B/2KRRS92./NQ.20P1REU90WCC7 SH11R:UHO8LROFYEV3$UJ:NF5M89IA$F82F+IS0CTBWJL0UDIV7:F--LM%V-1RLRHVIFRXS.:UVH7P+UEHC+38WS5I0";

    @Test
    void getCertificateModel() throws Exception {

        DecodeService ds = new DecodeService("c:/gamma/store");
        CertificateModel certificateModel = ds.getCertificateModel(testStringSecondDose);
        System.out.println(certificateModel);
    }


    @Test
    void getCertificateModelRecovery() throws Exception{
        DecodeService ds = new DecodeService("c:/gamma/store");
        CertificateModel certificateModel = ds.getCertificateModel(testRecovery);
        System.out.println(certificateModel);
    }

    @Test
    void getCertificateModelTestMolecular() throws Exception{
        DecodeService ds = new DecodeService(C_GAMMA_STORE);
        CertificateModel certificateModel = ds.getCertificateModel(testMolecularTest);
        System.out.println(certificateModel);
    }


}