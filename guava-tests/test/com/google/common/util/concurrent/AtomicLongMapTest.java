/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.testing.NullPointerTester;
import com.google.common.testing.SerializableTester;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.TestCase;

/**
 * Tests for {@link AtomicLongMap}.
 *
 * @author mike nonemacher
 */
@GwtCompatible(emulated = true)
public class AtomicLongMapTest extends TestCase {
  private static final int ITERATIONS = 100;
  private static final int MAX_ADDEND = 100;

  private final Random random = new Random(301);

  @GwtIncompatible // NullPointerTester
  public void testNulls() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicConstructors(AtomicLongMap.class);
    tester.testAllPublicStaticMethods(AtomicLongMap.class);
    AtomicLongMap<Object> map = AtomicLongMap.create();
    tester.testAllPublicInstanceMethods(map);
  }

  public void testCreate_map() {
    Map<String, Long> in = ImmutableMap.of("1", 1L, "2", 2L, "3", 3L);
    AtomicLongMap<String> map = AtomicLongMap.create(in);
    assertFalse(map.isEmpty());
    assertSame(3, map.size());
    assertTrue(map.containsKey("1"));
    assertTrue(map.containsKey("2"));
    assertTrue(map.containsKey("3"));
    assertEquals(1L, map.get("1"));
    assertEquals(2L, map.get("2"));
    assertEquals(3L, map.get("3"));
  }

  public void testIncrementAndGet() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      long result = map.incrementAndGet(key);
      long after = map.get(key);
      assertEquals(before + 1, after);
      assertEquals(after, result);
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
    assertEquals(ITERATIONS, (int) map.get(key));
  }

  public void testIncrementAndGet_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(1L, map.incrementAndGet(key));
    assertEquals(1L, map.get(key));

    assertEquals(0L, map.decrementAndGet(key));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(1L, map.incrementAndGet(key));
    assertEquals(1L, map.get(key));
  }

  public void testGetAndIncrement() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      long result = map.getAndIncrement(key);
      long after = map.get(key);
      assertEquals(before + 1, after);
      assertEquals(before, result);
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
    assertEquals(ITERATIONS, (int) map.get(key));
  }

  public void testGetAndIncrement_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(0L, map.getAndIncrement(key));
    assertEquals(1L, map.get(key));

    assertEquals(1L, map.getAndDecrement(key));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(0L, map.getAndIncrement(key));
    assertEquals(1L, map.get(key));
  }

  public void testDecrementAndGet() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      long result = map.decrementAndGet(key);
      long after = map.get(key);
      assertEquals(before - 1, after);
      assertEquals(after, result);
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
    assertEquals(-1 * ITERATIONS, (int) map.get(key));
  }

  public void testDecrementAndGet_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(-1L, map.decrementAndGet(key));
    assertEquals(-1L, map.get(key));

    assertEquals(0L, map.incrementAndGet(key));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(-1L, map.decrementAndGet(key));
    assertEquals(-1L, map.get(key));
  }

  public void testGetAndDecrement() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      long result = map.getAndDecrement(key);
      long after = map.get(key);
      assertEquals(before - 1, after);
      assertEquals(before, result);
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
    assertEquals(-1 * ITERATIONS, (int) map.get(key));
  }

  public void testGetAndDecrement_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(0L, map.getAndDecrement(key));
    assertEquals(-1L, map.get(key));

    assertEquals(-1L, map.getAndIncrement(key));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(0L, map.getAndDecrement(key));
    assertEquals(-1L, map.get(key));
  }

  public void testAddAndGet() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long addend = random.nextInt(MAX_ADDEND);
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      long result = map.addAndGet(key, addend);
      long after = map.get(key);
      assertEquals(before + addend, after);
      assertEquals(after, result);
      addend = after;
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
  }

  public void testAddAndGet_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long value = random.nextInt(MAX_ADDEND);
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(value, map.addAndGet(key, value));
    assertEquals(value, map.get(key));

    assertEquals(0L, map.addAndGet(key, -1 * value));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(value, map.addAndGet(key, value));
    assertEquals(value, map.get(key));
  }

  public void testGetAndAdd() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long addend = random.nextInt(MAX_ADDEND);
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      long result = map.getAndAdd(key, addend);
      long after = map.get(key);
      assertEquals(before + addend, after);
      assertEquals(before, result);
      addend = after;
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
  }

  public void testGetAndAdd_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long value = random.nextInt(MAX_ADDEND);
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(0L, map.getAndAdd(key, value));
    assertEquals(value, map.get(key));

    assertEquals(value, map.getAndAdd(key, -1 * value));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(0L, map.getAndAdd(key, value));
    assertEquals(value, map.get(key));
  }

  public void testPut() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long newValue = random.nextInt(MAX_ADDEND);
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      long result = map.put(key, newValue);
      long after = map.get(key);
      assertEquals(newValue, after);
      assertEquals(before, result);
      newValue += newValue;
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
  }

  public void testPut_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long value = random.nextInt(MAX_ADDEND);
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(0L, map.put(key, value));
    assertEquals(value, map.get(key));

    assertEquals(value, map.put(key, 0L));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(0L, map.put(key, value));
    assertEquals(value, map.get(key));
  }

  public void testPutAll() {
    Map<String, Long> in = ImmutableMap.of("1", 1L, "2", 2L, "3", 3L);
    AtomicLongMap<String> map = AtomicLongMap.create();
    assertTrue(map.isEmpty());
    assertSame(0, map.size());
    assertFalse(map.containsKey("1"));
    assertFalse(map.containsKey("2"));
    assertFalse(map.containsKey("3"));
    assertEquals(0L, map.get("1"));
    assertEquals(0L, map.get("2"));
    assertEquals(0L, map.get("3"));

    map.putAll(in);
    assertFalse(map.isEmpty());
    assertSame(3, map.size());
    assertTrue(map.containsKey("1"));
    assertTrue(map.containsKey("2"));
    assertTrue(map.containsKey("3"));
    assertEquals(1L, map.get("1"));
    assertEquals(2L, map.get("2"));
    assertEquals(3L, map.get("3"));
  }

  public void testPutIfAbsent() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long newValue = random.nextInt(MAX_ADDEND);
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      long result = map.putIfAbsent(key, newValue);
      long after = map.get(key);
      assertEquals(before, result);
      assertEquals(before == 0 ? newValue : before, after);

      map.remove(key);
      before = map.get(key);
      result = map.putIfAbsent(key, newValue);
      after = map.get(key);
      assertEquals(0, before);
      assertEquals(before, result);
      assertEquals(newValue, after);

      map.put(key, 0L);
      before = map.get(key);
      result = map.putIfAbsent(key, newValue);
      after = map.get(key);
      assertEquals(0, before);
      assertEquals(before, result);
      assertEquals(newValue, after);

      newValue += newValue;
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
  }

  public void testPutIfAbsent_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long value = random.nextInt(MAX_ADDEND);
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(0L, map.putIfAbsent(key, value));
    assertEquals(value, map.get(key));

    assertEquals(value, map.put(key, 0L));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(0L, map.putIfAbsent(key, value));
    assertEquals(value, map.get(key));
  }

  public void testReplace() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long newValue = random.nextInt(MAX_ADDEND);
    for (int i = 0; i < ITERATIONS; i++) {
      long before = map.get(key);
      assertFalse(map.replace(key, before + 1, newValue + 1));
      assertFalse(map.replace(key, before - 1, newValue - 1));
      assertTrue(map.replace(key, before, newValue));
      long after = map.get(key);
      assertEquals(newValue, after);
      newValue += newValue;
    }
    assertEquals(1, map.size());
    assertTrue(!map.isEmpty());
    assertTrue(map.containsKey(key));
  }

  public void testReplace_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    long value = random.nextInt(MAX_ADDEND);
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertTrue(map.replace(key, 0L, value));
    assertEquals(value, map.get(key));

    assertTrue(map.replace(key, value, 0L));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertTrue(map.replace(key, 0L, value));
    assertEquals(value, map.get(key));
  }

  public void testRemove() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0, map.size());
    assertTrue(map.isEmpty());
    assertEquals(0L, map.remove(key));

    long newValue = random.nextInt(MAX_ADDEND);
    for (int i = 0; i < ITERATIONS; i++) {
      map.put(key, newValue);
      assertTrue(map.containsKey(key));

      long before = map.get(key);
      long result = map.remove(key);
      long after = map.get(key);
      assertFalse(map.containsKey(key));
      assertEquals(before, result);
      assertEquals(0L, after);
      newValue += newValue;
    }
    assertEquals(0, map.size());
    assertTrue(map.isEmpty());
  }

  public void testRemove_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(0L, map.remove(key));
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(0L, map.put(key, 0L));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertEquals(0L, map.remove(key));
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));
  }

  public void testRemoveIfZero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0, map.size());
    assertTrue(map.isEmpty());
    assertFalse(map.removeIfZero(key));

    assertEquals(1, map.incrementAndGet(key));
    assertFalse(map.removeIfZero(key));
    assertEquals(2, map.incrementAndGet(key));
    assertFalse(map.removeIfZero(key));
    assertEquals(1, map.decrementAndGet(key));
    assertFalse(map.removeIfZero(key));
    assertEquals(0, map.decrementAndGet(key));
    assertTrue(map.removeIfZero(key));
    assertFalse(map.containsKey(key));
  }

  public void testRemoveValue() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0, map.size());
    assertTrue(map.isEmpty());
    assertFalse(map.remove(key, 0L));

    long newValue = random.nextInt(MAX_ADDEND);
    for (int i = 0; i < ITERATIONS; i++) {
      map.put(key, newValue);
      assertTrue(map.containsKey(key));

      long before = map.get(key);
      assertFalse(map.remove(key, newValue + 1));
      assertFalse(map.remove(key, newValue - 1));
      assertTrue(map.remove(key, newValue));
      long after = map.get(key);
      assertFalse(map.containsKey(key));
      assertEquals(0L, after);
      newValue += newValue;
    }
    assertEquals(0, map.size());
    assertTrue(map.isEmpty());
  }

  public void testRemoveValue_zero() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    String key = "key";
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertFalse(map.remove(key, 0L));
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));

    assertEquals(0L, map.put(key, 0L));
    assertEquals(0L, map.get(key));
    assertTrue(map.containsKey(key));

    assertTrue(map.remove(key, 0L));
    assertEquals(0L, map.get(key));
    assertFalse(map.containsKey(key));
  }
 
  // O teste serve para testar se o método removeallzeros() está fazendo o que ele deveria, que no caso é excluir todos os valores que são 0 de um map
  // O teste cria um map e um lista que sempre irá guardar objetos onde o resto da divisao dele seja  diferente de zero
  public void testRemoveZeros() {
    AtomicLongMap<Object> map = AtomicLongMap.create(); //Cria variável para setar os valores 0 e 1 das iterações.
    Set<Object> nonZeroKeys = Sets.newHashSet(); // cria variável para setar todos os valores das iterações sem zero.
    for (int i = 0; i < ITERATIONS; i++) { //Cria iteração de 0 até 100.
      Object key = new Object(); // Cria objeto chave.
      long value = i % 2; // Faz o mod da atual iteração com 2 (ou seja, o resto da divisão do valor atual da iteração, que será 0 ou 1)
      map.put(key, value); // Adiciona item no objeto map.
      if (value != 0L) { // Checa se o resto da divisão é diferente de 0
        nonZeroKeys.add(key); // Adiciona o objeto a nonzeroKeys sempre que o value for diferente 0.
      }
    }
    assertEquals(ITERATIONS, map.size()); //Checa se o valor da constante ITERATIONS setada acima (100) é igual ao tamanho do objeto map.
    assertTrue(map.asMap().containsValue(0L)); // Checa se no objeto map, contém o valor 0.

    map.removeAllZeros(); // Utiliza o método para remover todos os 0's do map.
    assertFalse(map.asMap().containsValue(0L)); //Checa se no objeto map não contém o valor 0.
    assertEquals(ITERATIONS / 2, map.size()); // Checa se o tamanho do objeto map é igual a metade da constante ITERATION dividido por 2.
    assertEquals(nonZeroKeys, map.asMap().keySet()); // Checa se os objetos de nonzeroKeys e map são iguais.
  }

  public void testRemoveZerosWithAtomicLongMapEmpty() {
    AtomicLongMap<Object> map = AtomicLongMap.create(); //Cria um AtomicLongMap para servir de dado para consumo da função.

    map.removeAllZeros(); //Utiliza método para remover todos os zeros.

    assertEquals(map.size(), 0); // Checa se o size do map é igual a zero.
  }

  public void testRemoveZerosWithAtomicLongMapOnlyZero() {
    AtomicLongMap<Object> map = AtomicLongMap.create(); //Cria um AtomicLongMap para servir de dado para consumo da função.
    Object key = new Object(); //Cria objeto para servir de chave para popular o map.

    for (int i=0; i < ITERATIONS ; i++) { //Cria iteração até 100
      map.put(key,0); // Adiciona objeto com valor zero no map.
    }

    map.removeAllZeros(); //Utiliza método para remover todos os zeros.
    assertEquals(map.size(), 0); // Checa se o size do map é igual a zero.
  }

  public void testRemoveZerosWithBooleanParam() {
    AtomicLongMap<Object> map = AtomicLongMap.create(); //Cria um AtomicLongMap para servir de dado para consumo da função.
    Object key = new Object(); //Cria objeto para servir de chave para popular o map.
    try { //Tenta popular o map com 100 valores true.
      for (int i=0; i < ITERATIONS ; i++) { //Cria iteração pra contar até 100
        map.put(key,true); // Adiciona valor true.
      }
      map.removeAllZeros(); // Utiliza método para remover todos os zeros.
    }catch (IllegalStateException e) { // Trata o erro que irá acontecer devido a diferença de parâmetros.
      // Não houve como fazer o map em um boolean.
    }

    assertNotNull(map); // Checa se o map ainda sim não é nulo.
    assertEquals(map.size(), 0); // Checa se size do map é igual a zero.
  }

  public void testClear() {
    AtomicLongMap<Object> map = AtomicLongMap.create();
    for (int i = 0; i < ITERATIONS; i++) {
      map.put(new Object(), i);
    }
    assertEquals(ITERATIONS, map.size());

    map.clear();
    assertEquals(0, map.size());
    assertTrue(map.isEmpty());
  }

  public void testSum() {
    AtomicLongMap<Object> map = AtomicLongMap.create();
    long sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
      map.put(new Object(), i);
      sum += i;
    }
    assertEquals(ITERATIONS, map.size());
    assertEquals(sum, map.sum());
  }

  public void testEmpty() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    assertEquals(0L, map.get("a"));
    assertEquals(0, map.size());
    assertTrue(map.isEmpty());
    assertFalse(map.remove("a", 1L));
    assertFalse(map.remove("a", 0L));
    assertFalse(map.replace("a", 1L, 0L));
  }

  public void testSerialization() {
    AtomicLongMap<String> map = AtomicLongMap.create();
    map.put("key", 1L);
    AtomicLongMap<String> reserialized = SerializableTester.reserialize(map);
    assertEquals(map.asMap(), reserialized.asMap());
  }
}
