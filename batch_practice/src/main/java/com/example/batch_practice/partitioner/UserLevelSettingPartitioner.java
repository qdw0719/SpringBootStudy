package com.example.batch_practice.partitioner;

import com.example.batch_practice.repository.UserRepository;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class UserLevelSettingPartitioner implements Partitioner {

    private final UserRepository userRepository;

    public UserLevelSettingPartitioner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        long minId = userRepository.findMinId();
        long maxId = userRepository.findMaxId();
        long targetSize = (maxId - minId) / gridSize + 1;

        Map<String, ExecutionContext> result = new HashMap<>();

        long num = 0;
        long start = minId;
        long end = start + targetSize -1;

        while(start <= maxId) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + num, value);

            if (end >= maxId) {
                end = maxId;
            }

            value.putLong("minId", start);
            value.putLong("maxId", end);

            start += targetSize;
            end += targetSize;
            num++;
        }
        return result;
    }
}
