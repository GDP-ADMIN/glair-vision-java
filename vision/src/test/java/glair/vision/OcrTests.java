package glair.vision;

import com.fasterxml.jackson.databind.JsonNode;
import glair.vision.api.Ocr;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.BpkbParam;
import glair.vision.model.param.KtpParam;
import glair.vision.util.Env;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OcrTests {
  private final Env env = TestsCommon.env;
  private final Ocr ocr = TestsCommon.vision.ocr();

  private static Stream<TestParams> endpointTestParams() {
    OcrTests instance = new OcrTests();

    String[] npwpKeys = {"alamat", "nama", "nik", "noNpwp"};
    TestParams npwp = new TestParams("npwp", instance.env.getNpwp(), npwpKeys);

    String[] kkKeys = {"alamat", "desa_kelurahan", "kabupaten_kota", "kecamatan",
        "kode_pos", "nama_kepala_keluarga", "nomor_blanko", "nomor_kk", "provinsi",
        "rt_rw"};
    TestParams kk = new TestParams("kk", instance.env.getKk(), kkKeys);

    String[] stnkKeys = {"alamat", "bahan_bakar", "berlaku_sampai", "isi_sillinder",
        "jenis", "kode_lokasi", "merk", "model", "nama_pemilik", "nomor_bpkb",
        "nomor_mesin", "nomor_rangka", "nomor_registrasi", "nomor_stnk",
        "nomor_urut_pendaftaran", "tahun_pembuatan", "tahun_registrasi", "tipe", "warna"
        , "warna_tnkb"};
    TestParams stnk = new TestParams("stnk", instance.env.getStnk(), stnkKeys);

    String[] passportKeys = {"birth_date", "birth_date_hash", "country", "doc_number",
        "doc_number_hash", "document_type", "expiry_date", "expiry_date_hash",
        "final_hash", "name", "nationality", "optional_data", "optional_data_hash",
        "sex", "surname"};
    TestParams passport = new TestParams("passport",
        instance.env.getPassport(),
        passportKeys
    );

    String[] plateKeys = {"plates"};
    TestParams licensePlate = new TestParams("licensePlate",
        instance.env.getLicensePlate(),
        plateKeys
    );

    String[] generalDocumentKeys = {"all_texts"};
    TestParams generalDocument = new TestParams("generalDocument",
        instance.env.getGeneralDocument(),
        generalDocumentKeys
    );

    String[] invoiceKeys = {"invoice_number", "invoice_date", "vendor_name",
        "invoice_total"};
    TestParams invoice = new TestParams("invoice",
        instance.env.getInvoice(),
        invoiceKeys
    );

    String[] receiptKeys = {"merchant_name", "receipt_date", "receipt_time",
        "total_amount"};
    TestParams receipt = new TestParams("receipt",
        instance.env.getReceipt(),
        receiptKeys
    );

    return Stream.of(npwp,
        kk,
        stnk,
        passport,
        licensePlate,
        generalDocument,
        invoice,
        receipt
    );
  }

  @ParameterizedTest
  @MethodSource("endpointTestParams")
  public void testEndpoint(TestParams testParams) {
    testWithScenarios(testParams.methodName, testParams.param, testParams::getAssert);
  }

  @Test
  public void testKtp() {
    String funName = "ktp";

    KtpParam param = new KtpParam(env.getKtp());
    KtpParam qualitiesParam = new KtpParam(env.getKtp(), true);
    KtpParam invalidFileParam = new KtpParam(env.getKtp() + "abc");

    BiFunction<Object, VisionSettings, String> fun = getFunction(funName);
    testWithScenarios(funName, param, this::assertKtpFields);
    TestsCommon.testSuccessScenario(fun,
        qualitiesParam,
        this::assertStatusAndReason,
        this::assertKtpQualitiesFields
    );
    TestsCommon.testFileNotFoundScenario(fun, invalidFileParam);
  }

  @Test
  public void testBpkb() {
    String funName = "bpkb";

    BpkbParam param = new BpkbParam(env.getBpkb());
    BpkbParam pageParam = new BpkbParam(env.getBpkb(), 1);
    BpkbParam invalidFileParam = new BpkbParam(env.getBpkb() + "abc");

    BiFunction<Object, VisionSettings, String> function = getFunction(funName);
    testWithScenarios(funName, param, this::assertBpkbFields);
    TestsCommon.testSuccessScenario(function,
        pageParam,
        this::assertStatusAndReason,
        this::assertBpkbPageFields
    );
    TestsCommon.testFileNotFoundScenario(function, invalidFileParam);
  }

  private void testWithScenarios(
      String methodName, Object param, Consumer<JsonNode> assertFieldsMethod
  ) {
    BiFunction<Object, VisionSettings, String> function = getFunction(methodName);

    TestsCommon.testSuccessScenario(function,
        param,
        this::assertStatusAndReason,
        assertFieldsMethod
    );

    TestsCommon.testInvalidCredentialScenario(function, param);

    if (param.getClass() == String.class) {
      TestsCommon.testFileNotFoundScenario(function, param + "abc");
    }
  }

  private void assertKtpFields(JsonNode jsonNode) {
    String[] imagesKeys = {"photo", "sign"};
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.get("images"), imagesKeys));

    String[] readKeys = {"agama", "alamat", "berlakuHingga", "golonganDarah",
        "jenisKelamin", "kecamatan", "kelurahanDesa", "kewarganegaraan", "kotaKabupaten"
        , "nama", "nik", "pekerjaan", "provinsi", "rtRw", "statusPerkawinan",
        "tanggalLahir", "tempatLahir"};
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.get("read"), readKeys));
  }

  private void assertKtpQualitiesFields(JsonNode jsonNode) {
    assertKtpFields(jsonNode);

    String[] qualitiesKeys = {"is_blurred", "is_bright", "is_copy", "is_cropped",
        "is_dark", "is_flash", "is_ktp", "is_rotated"};
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.get("qualities"), qualitiesKeys));
  }

  private void assertBpkbFields(JsonNode jsonNode) {
    String[] readKeys = {"identitas_pemilik", "identitas_kendaraan",
        "dokumen_registrasi_pertama", "halaman_terakhir"};

    String[] pemilikKeys = {"alamat", "alamat_email", "dikeluarkan", "nama_pemilik",
        "no_ktp_tdp", "nomor_bpkb", "pada_tanggal", "pekerjaan"};

    String[] kendaraanKeys = {"bahan_bakar", "isi_silinder", "jenis", "jumlah_roda",
        "jumlah_sumbu", "merk", "model", "nomor_mesin", "nomor_rangka",
        "nomor_registrasi", "tahun_pembuatan", "type", "warna"};

    String[] dokumenKeys = {"nama_apm", "nomor_faktur", "nomor_form_abc",
        "tanggal_faktur"};

    String[] terakhirKeys = {"diterbitkan_oleh", "no_register"};

    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.get("read"), readKeys));

    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.at("/read/identitas_pemilik"),
        pemilikKeys
    ));
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.at("/read/identitas_kendaraan"),
        kendaraanKeys
    ));
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.at("/read" +
            "/dokumen_registrasi_pertama"),
        dokumenKeys
    ));
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.at("/read/halaman_terakhir"),
        terakhirKeys
    ));
  }

  private void assertBpkbPageFields(JsonNode jsonNode) {
    String[] readKeys = {"identitas_pemilik"};

    String[] pemilikKeys = {"alamat", "alamat_email", "dikeluarkan", "nama_pemilik",
        "no_ktp_tdp", "nomor_bpkb", "pada_tanggal", "pekerjaan"};

    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.get("read"), readKeys));

    assertTrue(TestsCommon.checkAllKeyExist(jsonNode.at("/read/identitas_pemilik"),
        pemilikKeys
    ));
  }

  private void assertStatusAndReason(JsonNode jsonNode) {
    String[] outerKeys = {"status", "reason"};
    assertTrue(TestsCommon.checkAllKeyExist(jsonNode, outerKeys));
  }

  private static class TestParams {
    public String methodName;
    public Object param;
    public String[] dataKeys;

    public TestParams(
        String methodName, Object param, String[] dataKeys
    ) {
      this.methodName = methodName;
      this.param = param;
      this.dataKeys = dataKeys;
    }

    public void getAssert(JsonNode jsonNode) {
      assertTrue(TestsCommon.checkAllKeyExist(jsonNode.get("read"), dataKeys));
    }

    @Override
    public String toString() {
      return "Test - " + methodName;
    }
  }

  private BiFunction<Object, VisionSettings, String> getFunction(String methodName) {
    return (param, settings) -> {
      try {
        if (settings == null) {
          return ocr
              .getClass()
              .getMethod(methodName, param.getClass())
              .invoke(ocr, param)
              .toString();
        }
        return ocr
            .getClass()
            .getMethod(methodName, param.getClass(), settings.getClass())
            .invoke(ocr, param, settings)
            .toString();
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        throw new RuntimeException(cause);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    };
  }
}