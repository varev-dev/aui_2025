import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        var cluster1 = RenderCluster.builder()
                .id(UUID.randomUUID())
                .name("Cluster Alpha")
                .location("Warsaw")
                .build();

        var cluster2 = RenderCluster.builder()
                .id(UUID.randomUUID())
                .name("Cluster Beta")
                .location("Krakow")
                .build();

        var node1 = RenderNode.builder()
                .id(UUID.randomUUID())
                .hostname("node-a1")
                .gpuLoad(75.5)
                .temperature(68)
                .cluster(cluster1)
                .build();

        var node2 = RenderNode.builder()
                .id(UUID.randomUUID())
                .hostname("node-a2")
                .gpuLoad(42.7)
                .temperature(55)
                .cluster(cluster1)
                .build();

        var node3 = RenderNode.builder()
                .id(UUID.randomUUID())
                .hostname("node-b1")
                .gpuLoad(89.1)
                .temperature(72)
                .cluster(cluster2)
                .build();

        cluster1.getNodes().addAll(List.of(node1, node2));
        cluster2.getNodes().add(node3);

        List<RenderCluster> clusters = List.of(cluster1, cluster2);

        clusters.forEach(c -> {
            System.out.println(c);
            c.getNodes().forEach(n -> System.out.println("  " + n));
        });

        Set<RenderNode> allNodes = clusters.stream()
                .flatMap(c -> c.getNodes().stream())
                .collect(Collectors.toSet());

        System.out.println("\nAll nodes:");
        allNodes.forEach(System.out::println);

        System.out.println("\nFiltered and sorted (gpuLoad > 50, sort by temperature):");
        allNodes.stream()
                .filter(n -> n.getGpuLoad() > 50)
                .sorted(Comparator.comparingInt(RenderNode::getTemperature))
                .forEach(System.out::println);

        System.out.println("\nDTOs sorted by hostname:");
        List<RenderNodeDTO> dtos = allNodes.stream()
                .map(RenderNodeDTO::from)
                .sorted()
                .collect(Collectors.toList());
        dtos.forEach(System.out::println);

        try (var oos = new ObjectOutputStream(new FileOutputStream("clusters.bin"))) {
            oos.writeObject(clusters);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clusters.bin"))) {
            List<RenderCluster> deserialized = (List<RenderCluster>) ois.readObject();
            System.out.println("\nDeserialized data:");
            deserialized.forEach(c -> {
                System.out.println(c);
                c.getNodes().forEach(n -> System.out.println("  " + n));
            });
        }

        System.out.println("\nSimulating workload per cluster:");
        ForkJoinPool pool = new ForkJoinPool(2);
        pool.submit(() ->
            clusters.parallelStream().forEach(cluster -> {
                cluster.getNodes().forEach(node -> {
                    try {
                        Thread.sleep(200);
                        System.out.println("[" + cluster.getName() + "] processed " + node.getHostname());
                    } catch (InterruptedException ignored) {}
                });
            })
        ).join();
        pool.shutdown();
    }
}