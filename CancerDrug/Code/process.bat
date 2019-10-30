rem Generate MKN from regulation
java -cp bioInfo.jar;3rd-jars/ini4j-0.5.2.jar;3rd-jars/commons-lang3-3.5.jar bioinfo.regulation.Regulation2MKN settings.ini

rem Run evolution algorithm with MKN and attractors
java -cp bioInfo.jar;3rd-jars/ini4j-0.5.2.jar;3rd-jars/commons-lang3-3.5.jar bioinfo.challenge.EACancerState settings.ini

rem Generate training and question with MKN, attractors, drug targets and response
java -cp bioInfo.jar;3rd-jars/ini4j-0.5.2.jar;3rd-jars/commons-lang3-3.5.jar bioinfo.challenge.Drug2GeneExpression settings.ini

rem Training BP NN
java -cp bioInfo.jar;3rd-jars/ini4j-0.5.2.jar;3rd-jars/commons-lang3-3.5.jar;3rd-jars/encog-core-3.3.0.jar bioinfo.nn.NetworkTrainer settings.ini

rem question BP NN
java -cp bioInfo.jar;3rd-jars/ini4j-0.5.2.jar;3rd-jars/commons-lang3-3.5.jar;3rd-jars/encog-core-3.3.0.jar bioinfo.nn.QuestionExecutor settings.ini

rem summarize the BP NN result
java -cp bioInfo.jar;3rd-jars/ini4j-0.5.2.jar;3rd-jars/commons-lang3-3.5.jar;3rd-jars/encog-core-3.3.0.jar bioinfo.nn.DrugSynergy settings.ini

rem generate the benchmark according to actual synergy data
java -cp bioInfo.jar;3rd-jars/ini4j-0.5.2.jar;3rd-jars/commons-lang3-3.5.jar;3rd-jars/encog-core-3.3.0.jar bioinfo.challenge.SynergyBenchmark settings.ini
