package glair.vision;

import com.fasterxml.jackson.databind.JsonNode;
import glair.vision.api.Ocr;
import glair.vision.logger.LoggerConfig;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.BpkbParam;
import glair.vision.model.param.KtpParam;
import glair.vision.util.Env;
import glair.vision.util.Json;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OcrTests {
  private final Env env = new Env();
  private final VisionSettings visionSettings = new VisionSettings.Builder()
      .username(env.getUsername())
      .password(env.getPassword())
      .apiKey(env.getApiKey())
      .build();
  private final Ocr ocr = (new Vision(visionSettings,
      new LoggerConfig(LoggerConfig.INFO))).ocr();

  public OcrTests() throws Exception {}

  private static Stream<TestParams> endpointTestParams() throws Exception {
    OcrTests instance = new OcrTests();

    TestParams npwp = new TestParams("npwp",
        instance.env.getNpwp(),
        instance::assertNpwpFields);

    TestParams kk = new TestParams("kk", instance.env.getKk(), instance::assertKkFields);

    TestParams stnk = new TestParams("stnk",
        instance.env.getStnk(),
        instance::assertStnkFields);

    TestParams passport = new TestParams("passport",
        instance.env.getPassport(),
        instance::assertPassportFields);

    TestParams licensePlate = new TestParams("licensePlate",
        instance.env.getLicensePlate(),
        instance::assertLicensePlateFields);

    TestParams generalDocument = new TestParams("generalDocument",
        instance.env.getGeneralDocument(),
        instance::assertGeneralDocumentFields);

    TestParams invoice = new TestParams("invoice",
        instance.env.getInvoice(),
        instance::assertInvoiceFields);

    TestParams receipt = new TestParams("receipt",
        instance.env.getReceipt(),
        instance::assertReceiptFields);

    return Stream.of(
        npwp,
        kk,
        stnk,
        passport,
        licensePlate,
        generalDocument,
        invoice,
        receipt);
  }

//  @ParameterizedTest
  @MethodSource("endpointTestParams")
  public void testEndpoint(TestParams testParams) {
    testWithScenarios(testParams.methodName,
        testParams.param,
        testParams.assertFieldsMethod);
  }

//  @Test
  public void testKtp() {
    BiFunction<Object, VisionSettings, String> function = getFunction("ktp");

    KtpParam param = new KtpParam(env.getKtp());
    KtpParam qualitiesParam = new KtpParam(env.getKtp(), true);
    KtpParam invalidFileParam = new KtpParam(env.getKtp() + "abc");

    testWithScenarios("ktp", param, this::assertKtpFields);
    TestsCommon.testSuccessScenario(function,
        qualitiesParam,
        this::assertStatusAndReason,
        this::assertKtpQualitiesFields);
    TestsCommon.testFileNotFoundScenario(function, invalidFileParam);
  }

//  @Test
  public void testBpkb() {
    BiFunction<Object, VisionSettings, String> function = getFunction("bpkb");

    BpkbParam param = new BpkbParam(env.getBpkb());
    BpkbParam pageParam = new BpkbParam(env.getBpkb(), 1);
    BpkbParam invalidFileParam = new BpkbParam(env.getBpkb() + "abc");

    testWithScenarios("bpkb", param, this::assertBpkbFields);
    TestsCommon.testSuccessScenario(function,
        pageParam,
        this::assertStatusAndReason,
        this::assertBpkbPageFields);
    TestsCommon.testFileNotFoundScenario(function, invalidFileParam);
  }

  private void testWithScenarios(String methodName, Object param,
                                 Consumer<JsonNode> assertFieldsMethod) {
    BiFunction<Object, VisionSettings, String> function = getFunction(methodName);

    TestsCommon.testSuccessScenario(function,
        param,
        this::assertStatusAndReason,
        assertFieldsMethod);

    TestsCommon.testInvalidCredentialScenario(function, param);

    if (param.getClass() == String.class) {
      TestsCommon.testFileNotFoundScenario(function, param + "abc");
    }
  }

  private void assertKtpFields(JsonNode jsonNode) {
    String[] imagesKeys = {"photo", "sign"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("images"), imagesKeys));

    String[] readKeys = {"agama", "alamat", "berlakuHingga", "golonganDarah",
        "jenisKelamin", "kecamatan", "kelurahanDesa", "kewarganegaraan", "kotaKabupaten"
        , "nama", "nik", "pekerjaan", "provinsi", "rtRw", "statusPerkawinan",
        "tanggalLahir", "tempatLahir"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));

    assertConfidenceValue(jsonNode, "/read/nik");
  }

  private void assertKtpQualitiesFields(JsonNode jsonNode) {
    assertKtpFields(jsonNode);

    String[] qualitiesKeys = {"is_blurred", "is_bright", "is_copy", "is_cropped",
        "is_dark", "is_flash", "is_ktp", "is_rotated"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("qualities"), qualitiesKeys));
  }

  private void assertNpwpFields(JsonNode jsonNode) {
    String[] readKeys = {"alamat", "nama", "nik", "noNpwp"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));
    assertConfidenceValue(jsonNode, "/read/nama");
  }

  private void assertKkFields(JsonNode jsonNode) {
    String[] readKeys = {"alamat", "desa_kelurahan", "kabupaten_kota", "kecamatan",
        "kode_pos", "nama_kepala_keluarga", "nomor_blanko", "nomor_kk", "provinsi",
        "rt_rw"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));
  }

  private void assertStnkFields(JsonNode jsonNode) {
    String[] readKeys = {"alamat", "bahan_bakar", "berlaku_sampai", "isi_sillinder",
        "jenis", "kode_lokasi", "merk", "model", "nama_pemilik", "nomor_bpkb",
        "nomor_mesin", "nomor_rangka", "nomor_registrasi", "nomor_stnk",
        "nomor_urut_pendaftaran", "tahun_pembuatan", "tahun_registrasi", "tipe", "warna"
        , "warna_tnkb"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));
  }

  private void assertPassportFields(JsonNode jsonNode) {
    String[] readKeys = {"birth_date", "birth_date_hash", "country", "doc_number",
        "doc_number_hash", "document_type", "expiry_date", "expiry_date_hash",
        "final_hash", "name", "nationality", "optional_data", "optional_data_hash",
        "sex", "surname"};

    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));
  }

  private void assertLicensePlateFields(JsonNode jsonNode) {
    String[] readKeys = {"plates"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));
  }

  private void assertGeneralDocumentFields(JsonNode jsonNode) {
    String[] readKeys = {"all_texts"};
    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));
  }

  private void assertInvoiceFields(JsonNode jsonNode) {
    String[] readKeys = {"invoice_number", "invoice_date", "vendor_name",
        "invoice_total"};

    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));
  }

  private void assertReceiptFields(JsonNode jsonNode) {
    String[] readKeys = {"merchant_name", "receipt_date", "receipt_time", "total_amount"};

    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));
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

    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));

    assertTrue(Json.checkAllKeyExist(jsonNode.at("/read/identitas_pemilik"),
        pemilikKeys));
    assertTrue(Json.checkAllKeyExist(jsonNode.at("/read/identitas_kendaraan"),
        kendaraanKeys));
    assertTrue(Json.checkAllKeyExist(jsonNode.at("/read/dokumen_registrasi_pertama"),
        dokumenKeys));
    assertTrue(Json.checkAllKeyExist(jsonNode.at("/read/halaman_terakhir"),
        terakhirKeys));
  }

  private void assertBpkbPageFields(JsonNode jsonNode) {
    String[] readKeys = {"identitas_pemilik"};

    String[] pemilikKeys = {"alamat", "alamat_email", "dikeluarkan", "nama_pemilik",
        "no_ktp_tdp", "nomor_bpkb", "pada_tanggal", "pekerjaan"};

    assertTrue(Json.checkAllKeyExist(jsonNode.get("read"), readKeys));

    assertTrue(Json.checkAllKeyExist(jsonNode.at("/read/identitas_pemilik"),
        pemilikKeys));
  }

  private void assertConfidenceValue(JsonNode jsonNode, String field) {
    JsonNode node = jsonNode.at(field);
    assertTrue(node.isObject());
    assertTrue(node.has("confidence") && node.get("confidence").isNumber());
    assertTrue(node.has("value") && node.get("value").isTextual());
  }

  private void assertStatusAndReason(JsonNode jsonNode) {
    assertTrue(jsonNode.has("status") && jsonNode.get("status").isTextual());
    assertTrue(jsonNode.has("reason") && jsonNode.get("reason").isTextual());
  }

  private record TestParams(String methodName, Object param,
                            Consumer<JsonNode> assertFieldsMethod) {
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