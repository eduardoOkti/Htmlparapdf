package com.eongarelli.htmlparapdf;

import android.content.Context;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@DesignerComponent(
        version = 1,
        description = "Extensão otimizada para salvar arquivos no ASD para o KOMBY.",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        icon = ""
)
@SimpleObject(external = true)
public class Htmlparapdf extends AndroidNonvisibleComponent {

    private final Context context;

    public Htmlparapdf(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

    @SimpleFunction(description = "Salva o conteúdo HTML diretamente em um arquivo no ASD com nome fixo.")
    public void SalvarHtmlNoAsd(String htmlContent, String nomeArquivo) {
        try {
            File pastaAsd = context.getExternalFilesDir(null);
            if (pastaAsd != null && !pastaAsd.exists()) {
                pastaAsd.mkdirs();
            }

            File arquivoPdf = new File(pastaAsd, nomeArquivo);

            FileOutputStream fos = new FileOutputStream(arquivoPdf);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(htmlContent);
            writer.flush();
            writer.close();
            fos.close();

            ArquivoSalvoComSucesso(arquivoPdf.getAbsolutePath());
        } catch (Exception e) {
            ErroAoSalvar(e.getMessage());
        }
    }

    @SimpleEvent(description = "Disparado quando o arquivo é salvo com sucesso no ASD.")
    public void ArquivoSalvoComSucesso(String caminhoCompleto) {
        EventDispatcher.dispatchEvent(this, "ArquivoSalvoComSucesso", caminhoCompleto);
    }

    @SimpleEvent(description = "Disparado se ocorrer algum erro ao salvar.")
    public void ErroAoSalvar(String mensagemErro) {
        EventDispatcher.dispatchEvent(this, "ErroAoSalvar", mensagemErro);
    }
}
