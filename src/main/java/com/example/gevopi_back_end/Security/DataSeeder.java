package com.example.gevopi_back_end.Security;

import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final HistorialClinicoRepository historialClinicoRepository;
    private final TestRepository testRepository;
    private final PreguntaRepository preguntaRepository;

    public DataSeeder(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            HistorialClinicoRepository historialClinicoRepository,
            TestRepository testRepository,
            PreguntaRepository preguntaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.historialClinicoRepository = historialClinicoRepository;
        this.testRepository = testRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedUsuarios();
        seedHistorialesClinicos();
        seedTestsYPreguntas();
    }

    private void seedRoles() {

        List<String> nombres = List.of("Super Admiin", "Admin", "Voluntario");

        for (String nombre : nombres) {
            if (rolRepository.findByNombre(nombre).isEmpty()) {
                Rol rol = new Rol();
                rol.setNombre(nombre);
                rolRepository.save(rol);
            }
        }
    }

    private void seedUsuarios() {
        if (!usuarioRepository.existsByEmail("franco.torrez.alv@gmail.com")) {
            Rol rolVoluntario = rolRepository.findByNombre("Super Admiin")
                    .orElseThrow(() -> new RuntimeException("Rol 'Voluntario' no encontrado"));

            Usuario u1 = new Usuario(0, "Franco Javier", "Torrez Alvarado", "franco.torrez.alv@gmail.com", "9782951", "76636677", "12345", true, Set.of(rolVoluntario));
            Usuario u2 = new Usuario(0, "Alejandro", "Ormachea Castrillo", "alejandroormachea05@gmail.com", "9053438", "76624444", "12345", true, Set.of(rolVoluntario));
            Usuario u3 = new Usuario(0, "Matias Ricardo", "Mendoza Peducassé", "matiasrmendozap05@gmail.com", "9604708", "77077295", "12345", true, Set.of(rolVoluntario));

            usuarioRepository.saveAll(List.of(u1, u2, u3));
        }
    }

    private void seedHistorialesClinicos() {
        for (int i = 1; i <= 7; i++) {
            if (!historialClinicoRepository.existsById(i)) {
                LocalDateTime fecha = LocalDateTime.of(2025, 5, 11, 11, 17, 19);
                HistorialClinico historial = new HistorialClinico(
                        i,
                        fecha,
                        fecha,
                        new HashSet<>(),
                        "franco.torrez.alv@gmail.com"
                );
                historialClinicoRepository.save(historial);
            }
        }
    }

    private void seedTestsYPreguntas() {
        if (testRepository.count() < 2) {
            Test dummy1 = new Test();
            dummy1.setNombre("Dummy1");
            dummy1.setDescripcion("Dummy");
            dummy1.setCategoria("Dummy");

            Test dummy2 = new Test();
            dummy2.setNombre("Dummy2");
            dummy2.setDescripcion("Dummy");
            dummy2.setCategoria("Dummy");

            testRepository.saveAll(List.of(dummy1, dummy2));
            testRepository.deleteAll(List.of(dummy1, dummy2));
        }

        Test fisico = testRepository.findByNombre("Fisico")
                .orElseGet(() -> testRepository.save(new Test(0, "Fisico",
                        "Revisión médica para detectar daños como fatiga, lesiones, problemas respiratorios o quemaduras tras una intervención, y asegurar una recuperación adecuada.",
                        "Fisico")));

        Test psicologico = testRepository.findByNombre("Psicologico")
                .orElseGet(() -> testRepository.save(new Test(0, "Psicologico",
                        "Detecta el impacto emocional tras un incendio, evaluando ansiedad, insomnio, estrés o pensamientos intrusivos para brindar apoyo oportuno.",
                        "Psicologico")));

        String[] preguntasFisicas = {
                "¿Te sientes más cansado o agotado de lo habitual después de las intervenciones?",
                "¿Has notado quemaduras, irritación o enrojecimiento en la piel después de las intervenciones?",
                "¿Has tenido dificultades para respirar o tos después de las intervenciones?",
                "¿Tienes dolor o molestias en el pecho desde el incendio?",
                "¿Has experimentado palpitaciones o un ritmo cardíaco irregular después de la intervención?",
                "¿Tus ojos han estado irritados, con ardor o picazón desde la intervención?",
                "¿Tienes dificultad para respirar profundamente desde la intervención?",
                "¿Haz notado que tu nariz está congestionada o bloqueada más de lo normal?",
                "¿Como te encuentras del brazo izquierdo?",
                "¿Como te encuentras del brazo derecho?",
                "¿Como te encuentras de la pierna izquierda?",
                "¿Como te encuentras de la pierna derecha?",
                "¿Como te encuentras del torso?",
                "¿Como te encuentras de la cabeza?"
        };

        String[] preguntasPsicologicas = {
                "¿Con qué frecuencia has tenido pensamientos no deseados relacionados al incendio?",
                "¿Sientes que últimamente piensas en qué pudiste hacer diferente durante la intervención?",
                "¿Has notado disminución de apetito desde la intervención?",
                "¿Te resulta difícil relajarte o desconectar mentalmente después de las intervenciones?",
                "¿Has tenido dificultades para concentrarte en tus tareas diarias debido al estrés?",
                "¿Has sufrido de insomnio recientemente?",
                "¿Te has sentido emocionalmente más inestable o irritable desde el incendio?",
                "¿Te sientes preocupado o ansioso constantemente desde el incendio?"
        };

        List<Pregunta> nuevas = new ArrayList<>();

        for (String texto : preguntasFisicas) {
            if (!preguntaRepository.existsByTextoAndTest(texto, fisico)) {
                nuevas.add(new Pregunta(0, fisico, texto, "Numerica (1-5)"));
            }
        }

        for (String texto : preguntasPsicologicas) {
            if (!preguntaRepository.existsByTextoAndTest(texto, psicologico)) {
                nuevas.add(new Pregunta(0, psicologico, texto, "Numerica (1-5)"));
            }
        }

        if (!nuevas.isEmpty()) {
            preguntaRepository.saveAll(nuevas);
        }
    }
}
