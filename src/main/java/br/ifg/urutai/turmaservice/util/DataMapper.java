
package br.ifg.urutai.turmaservice.util;

import br.ifg.urutai.turmaservice.dto.CursoRequisicao;
import br.ifg.urutai.turmaservice.dto.CursoResposta;
import br.ifg.urutai.turmaservice.model.Curso;
import org.modelmapper.convention.MatchingStrategies;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataMapper {

    private static final ModelMapper mapper = new ModelMapper();

    static {
        configurarMapper();
    }

    private static void configurarMapper() {

        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(true)
                .setSkipNullEnabled(true);

        mapper.createTypeMap(
                CursoRequisicao.class,
                Curso.class
        ).addMappings(mapeamento -> {
            mapeamento.skip(Curso::setId);
            mapeamento.skip(Curso::setInstituicao);
        });

        mapper.createTypeMap(
                Curso.class,
                CursoResposta.class
        ).addMappings(mapeamento ->
                mapeamento.skip(CursoResposta::setInstituicaoId)
        );
    }

    public static <T> T parseObject(
            Object origem,
            Class<T> destino) {

        if (origem == null) {
            return null;
        }

        return mapper.map(origem, destino);
    }

    public static <O, D> List<D> parseListObjects(
            List<O> origem,
            Class<D> destino) {

        List<D> objetosConvertidos = new ArrayList<>();

        if (origem == null) {
            return objetosConvertidos;
        }

        for (O objeto : origem) {
            objetosConvertidos.add(
                    parseObject(objeto, destino)
            );
        }

        return objetosConvertidos;
    }

    public static void copiarPropriedades(
            Object origem,
            Object destino) {

        if (origem == null) {
            return;
        }

        if (destino == null) {
            throw new IllegalArgumentException(
                    "O objeto de destino não pode ser nulo"
            );
        }

        mapper.map(origem, destino);
    }
}
